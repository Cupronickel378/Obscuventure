package com.obscuventure.obscuventure;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModInit {
    public static final String MODID = "obscuventure";

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<Block> MYSTIC_BLOCK = BLOCKS.register("mystic_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3.0f, 6.0f)
                    .sound(SoundType.STONE)));

    public static final RegistryObject<Item> MYSTIC_GEM = ITEMS.register("mystic_gem",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Block> DUNGEON_CORE = BLOCKS.register("dungeon_core",
            () -> new DungeonCoreBlock(BlockBehaviour.Properties.of()
                    .strength(6.0f, 1200.0f)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> 12)
                    .sound(SoundType.NETHERITE_BLOCK)));

    public static final RegistryObject<Item> DUNGEON_CORE_ITEM = ITEMS.register("dungeon_core",
            () -> new BlockItem(DUNGEON_CORE.get(), new Item.Properties()));

    public static final RegistryObject<CreativeModeTab> OBSCUVENTURE_TAB = CREATIVE_MODE_TABS.register("obscuventure_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(MYSTIC_GEM.get()))
                    .title(Component.translatable("creativetab.obscuventure"))
                    .displayItems((parameters, output) -> {
                        output.accept(MYSTIC_GEM.get());
                        output.accept(MYSTIC_BLOCK.get());
                        output.accept(DUNGEON_CORE_ITEM.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        CREATIVE_MODE_TABS.register(eventBus);
    }
}