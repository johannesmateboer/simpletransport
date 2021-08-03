package net.pattox.simpletransport;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.pattox.simpletransport.block.*;
import net.pattox.simpletransport.entity.ConveyorDetectorEntity;
import net.pattox.simpletransport.entity.ConveyorEntity;
import net.pattox.simpletransport.entity.ExtractorEntity;

public class SimpleTransport implements ModInitializer {

	public static final String MOD_ID = "simpletransport";

	public static final Block CONVEYOR;
	public static final BlockItem CONVEYOR_ITEM;
	public static BlockEntityType<ConveyorEntity> CONVEYOR_ENTITY;
	public static final Identifier CONVEYOR_IDENTIFIER = new Identifier(MOD_ID, "conveyor");

	public static final Block EXTRACTOR;
	public static final BlockItem EXTRACTOR_ITEM;
	public static BlockEntityType<ExtractorEntity> EXTRACTOR_ENTITY;
	public static final Identifier EXTRACTOR_IDENTIFIER = new Identifier(MOD_ID, "extractor");

	public static final Block CONVEYOR_DETECTOR;
	public static final BlockItem CONVEYOR_DETECTOR_ITEM;
	public static BlockEntityType<ConveyorDetectorEntity> CONVEYOR_DETECTOR_ENTITY;
	public static final Identifier CONVEYOR_DETECTOR_IDENTIFIER = new Identifier(MOD_ID, "conveyor_detector");

	public static final Block INSERTER;
	public static final BlockItem INSERTER_ITEM;
	public static final Identifier INSERTER_IDENTIFIER = new Identifier(MOD_ID, "inserter");

	public static final Block DESTRUCTOR;
	public static final BlockItem DESTRUCTOR_ITEM;
	public static final Identifier DESTRUCTOR_IDENTIFIER = new Identifier(MOD_ID, "destructor");

	// Set the ItemGroup
	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(
					new Identifier(MOD_ID, "general"))
			.icon(() -> new ItemStack(SimpleTransport.CONVEYOR))
			.build();

	static {
		CONVEYOR = Registry.register(Registry.BLOCK, CONVEYOR_IDENTIFIER, new Conveyor(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).nonOpaque()));
		CONVEYOR_ITEM = Registry.register(Registry.ITEM, CONVEYOR_IDENTIFIER, new BlockItem(CONVEYOR, new FabricItemSettings().group(SimpleTransport.ITEM_GROUP)));
		CONVEYOR_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, CONVEYOR_IDENTIFIER, FabricBlockEntityTypeBuilder.create(ConveyorEntity::new, CONVEYOR).build(null));

		CONVEYOR_DETECTOR = Registry.register(Registry.BLOCK, CONVEYOR_DETECTOR_IDENTIFIER, new ConveyorDetector(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).nonOpaque()));
		CONVEYOR_DETECTOR_ITEM = Registry.register(Registry.ITEM, CONVEYOR_DETECTOR_IDENTIFIER, new BlockItem(CONVEYOR_DETECTOR, new FabricItemSettings().group(SimpleTransport.ITEM_GROUP)));
		CONVEYOR_DETECTOR_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, CONVEYOR_DETECTOR_IDENTIFIER, FabricBlockEntityTypeBuilder.create(ConveyorDetectorEntity::new, CONVEYOR_DETECTOR).build(null));

		EXTRACTOR = Registry.register(Registry.BLOCK, EXTRACTOR_IDENTIFIER, new Extractor(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).nonOpaque()));
		EXTRACTOR_ITEM = Registry.register(Registry.ITEM, EXTRACTOR_IDENTIFIER, new BlockItem(EXTRACTOR, new FabricItemSettings().group(SimpleTransport.ITEM_GROUP)));
		EXTRACTOR_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, EXTRACTOR_IDENTIFIER, FabricBlockEntityTypeBuilder.create(ExtractorEntity::new, EXTRACTOR).build(null));

		INSERTER = Registry.register(Registry.BLOCK, INSERTER_IDENTIFIER, new Inserter(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).nonOpaque()));
		INSERTER_ITEM = Registry.register(Registry.ITEM, INSERTER_IDENTIFIER, new BlockItem(INSERTER, new FabricItemSettings().group(SimpleTransport.ITEM_GROUP)));

		DESTRUCTOR = Registry.register(Registry.BLOCK, DESTRUCTOR_IDENTIFIER, new Destructor(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).nonOpaque()));
		DESTRUCTOR_ITEM = Registry.register(Registry.ITEM, DESTRUCTOR_IDENTIFIER, new BlockItem(DESTRUCTOR, new FabricItemSettings().group(SimpleTransport.ITEM_GROUP)));

	}

	@Override
	public void onInitialize() {

	}
}
