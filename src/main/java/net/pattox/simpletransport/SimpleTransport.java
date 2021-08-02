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
import net.pattox.simpletransport.block.Conveyor;
import net.pattox.simpletransport.block.Extractor;
import net.pattox.simpletransport.block.Inserter;
import net.pattox.simpletransport.entity.ExtractorEntity;

public class SimpleTransport implements ModInitializer {

	public static final String MOD_ID = "simpletransport";

	public static final Block CONVEYOR;
	public static final BlockItem CONVEYOR_ITEM;
	public static final Identifier CONVEYOR_IDENTIFIER = new Identifier(MOD_ID, "conveyor");

	public static final Block EXTRACTOR;
	public static final BlockItem EXTRACTOR_ITEM;
	public static BlockEntityType<ExtractorEntity> EXTRACTOR_ENTITY;
	public static final Identifier EXTRACTOR_IDENTIFIER = new Identifier(MOD_ID, "extractor");

	public static final Block INSERTER;
	public static final BlockItem INSERTER_ITEM;
	//public static BlockEntityType<InserterEntity> INSERTER_ENTITY;
	public static final Identifier INSERTER_IDENTIFIER = new Identifier(MOD_ID, "inserter");

	// Set the ItemGroup
	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(
					new Identifier(MOD_ID, "general"))
			.icon(() -> new ItemStack(Blocks.CHEST))
			.build();

	static {
		CONVEYOR = Registry.register(Registry.BLOCK, CONVEYOR_IDENTIFIER, new Conveyor(FabricBlockSettings.copyOf(Blocks.ACACIA_WOOD).nonOpaque()));
		CONVEYOR_ITEM = Registry.register(Registry.ITEM, CONVEYOR_IDENTIFIER, new BlockItem(CONVEYOR, new FabricItemSettings().group(SimpleTransport.ITEM_GROUP)));

		EXTRACTOR = Registry.register(Registry.BLOCK, EXTRACTOR_IDENTIFIER, new Extractor(FabricBlockSettings.copyOf(Blocks.ACACIA_WOOD).nonOpaque()));
		EXTRACTOR_ITEM = Registry.register(Registry.ITEM, EXTRACTOR_IDENTIFIER, new BlockItem(EXTRACTOR, new FabricItemSettings().group(SimpleTransport.ITEM_GROUP)));
		EXTRACTOR_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, EXTRACTOR_IDENTIFIER, FabricBlockEntityTypeBuilder.create(ExtractorEntity::new, EXTRACTOR).build(null));

		INSERTER = Registry.register(Registry.BLOCK, INSERTER_IDENTIFIER, new Inserter(FabricBlockSettings.copyOf(Blocks.ACACIA_WOOD).nonOpaque()));
		INSERTER_ITEM = Registry.register(Registry.ITEM, INSERTER_IDENTIFIER, new BlockItem(INSERTER, new FabricItemSettings().group(SimpleTransport.ITEM_GROUP)));
		//INSERTER_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, INSERTER_IDENTIFIER, FabricBlockEntityTypeBuilder.create(InserterEntity::new, EXTRACTOR).build(null));
	}

	@Override
	public void onInitialize() {




	}
}
