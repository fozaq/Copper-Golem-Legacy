package com.github.smallinger.coppergolemlegacy.block;

import com.github.smallinger.coppergolemlegacy.CopperGolemLegacy;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.ChangeOverTimeBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Optional;
import java.util.function.Supplier;

public class CopperButtonBlock extends ButtonBlock implements WeatheringCopper {
    private final WeatheringCopper.WeatherState weatherState;
    private Supplier<WaxedCopperButtonBlock> waxedButton;

    public CopperButtonBlock(WeatheringCopper.WeatherState weatherState, Properties properties) {
        super(BlockSetType.COPPER, 15, properties); // 15 ticks = 1.5 seconds activation time
        this.weatherState = weatherState;
    }

    public void setWaxedButton(Supplier<WaxedCopperButtonBlock> waxedButton) {
        this.waxedButton = waxedButton;
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        // Check if player is using honeycomb to wax the button
        if (stack.is(Items.HONEYCOMB) && waxedButton != null) {
            level.playSound(player, pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.levelEvent(player, 3003, pos, 0);
            
            if (!level.isClientSide) {
                // Replace with waxed version, preserving button state
                BlockState waxedState = waxedButton.get().defaultBlockState()
                    .setValue(FACING, state.getValue(FACING))
                    .setValue(POWERED, state.getValue(POWERED))
                    .setValue(FACE, state.getValue(FACE));
                level.setBlock(pos, waxedState, 11);
                
                if (player != null && !player.isCreative()) {
                    stack.consume(1, player);
                }
            }
            
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        
        // Check if player is using an axe to scrape oxidation
        if (stack.is(ItemTags.AXES)) {
            Block previousBlock = null;
            
            // Determine the previous oxidation state
            if (this == CopperGolemLegacy.OXIDIZED_COPPER_BUTTON.get()) {
                previousBlock = CopperGolemLegacy.WEATHERED_COPPER_BUTTON.get();
            } else if (this == CopperGolemLegacy.WEATHERED_COPPER_BUTTON.get()) {
                previousBlock = CopperGolemLegacy.EXPOSED_COPPER_BUTTON.get();
            } else if (this == CopperGolemLegacy.EXPOSED_COPPER_BUTTON.get()) {
                previousBlock = CopperGolemLegacy.COPPER_BUTTON.get();
            }
            
            if (previousBlock != null) {
                level.playSound(player, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.levelEvent(player, 3005, pos, 0);
                
                if (!level.isClientSide) {
                    // Replace with previous oxidation state, preserving button state
                    BlockState newState = previousBlock.defaultBlockState()
                        .setValue(FACING, state.getValue(FACING))
                        .setValue(POWERED, state.getValue(POWERED))
                        .setValue(FACE, state.getValue(FACE));
                    level.setBlock(pos, newState, 11);
                    
                    if (player != null && !player.isCreative()) {
                        stack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(stack));
                    }
                }
                
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        // Oxidized buttons cannot be pressed
        if (this.weatherState == WeatherState.OXIDIZED) {
            // Play hit sound when trying to use oxidized button
            level.playSound(player, pos, SoundEvents.COPPER_HIT, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.PASS;
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return Optional.ofNullable(WeatheringCopper.getNext(state.getBlock())).isPresent();
    }
}
