package com.obscuventure.obscuventure;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class DungeonCoreBlock extends Block {
    public DungeonCoreBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            ServerLevel serverLevel = (ServerLevel) level;

            double radius = 64.0;
            AABB area = new AABB(pos).inflate(radius);
            List<ServerPlayer> playersNearby = serverLevel.getEntitiesOfClass(ServerPlayer.class, area);

            for (ServerPlayer partyPlayer : playersNearby) {
                partyPlayer.getPersistentData().putBoolean("dungeon_cleared", true);

                if (partyPlayer.hasEffect(MobEffects.DIG_SLOWDOWN)) {
                    partyPlayer.removeEffect(MobEffects.DIG_SLOWDOWN);
                }

                partyPlayer.connection.send(new ClientboundSetTitleTextPacket(Component.literal("§6Проклятие рассеялось")));
                partyPlayer.connection.send(new ClientboundSetSubtitleTextPacket(Component.literal("§7Стены цитадели поддаются вашей воле...")));
            }

            level.playSound(null, pos, SoundEvents.END_PORTAL_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);

            ItemStack rewardBag = new ItemStack(ModInit.CURSED_BAG.get());
            ItemEntity entity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, rewardBag);
            level.addFreshEntity(entity);

            level.destroyBlock(pos, false);

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}