package com.obscuventure.obscuventure;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.Random;

public class CursedBagItem extends Item {
    public CursedBagItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        if (!level.isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) level;
            Random random = new Random();
            
            Item rewardItem = Items.GOLD_INGOT;
            int count = 3 + random.nextInt(5);

            int chance = random.nextInt(100);
            if (chance < 10) {
                rewardItem = Items.NETHERITE_INGOT;
                count = 1;
            } else if (chance < 35) {
                rewardItem = Items.DIAMOND;
                count = 1 + random.nextInt(3);
            } else if (chance < 65) {
                rewardItem = Items.EMERALD;
                count = 2 + random.nextInt(4);
            }

            ItemStack rewardStack = new ItemStack(rewardItem, count);
            ItemEntity itemEntity = new ItemEntity(serverLevel, player.getX(), player.getY() + 0.5, player.getZ(), rewardStack);
            serverLevel.addFreshEntity(itemEntity);

            level.playSound(null, player.blockPosition(), SoundEvents.BUNDLE_DROP_CONTENTS, SoundSource.PLAYERS, 1.0F, 1.0F);

            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
        }
        
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
}