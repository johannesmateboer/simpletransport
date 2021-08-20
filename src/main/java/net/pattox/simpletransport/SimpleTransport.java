package net.pattox.simpletransport;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.pattox.simpletransport.block.*;
import net.pattox.simpletransport.entity.*;
import net.pattox.simpletransport.gui.GenericFilterScreenHandler;

public class SimpleTransport implements ModInitializer {

	public static final String MOD_ID = "simpletransport";

	public static final Block CONVEYOR;
	public static final BlockItem CONVEYOR_ITEM;
	public static BlockEntityType<ConveyorEntity> CONVEYOR_ENTITY;
	public static final Identifier CONVEYOR_IDENTIFIER = new Identifier(MOD_ID, "conveyor");

	public static final Block CONVEYOR_DEMAG;
	public static final BlockItem CONVEYOR_DEMAG_ITEM;
	public static BlockEntityType<ConveyorDemagEntity> CONVEYOR_DEMAG_ENTITY;
	public static final Identifier CONVEYOR_DEMAG_IDENTIFIER = new Identifier(MOD_ID, "conveyor_demag");

	public static final Block EXTRACTOR;
	public static final BlockItem EXTRACTOR_ITEM;
	public static BlockEntityType<ExtractorEntity> EXTRACTOR_ENTITY;
	public static final Identifier EXTRACTOR_IDENTIFIER = new Identifier(MOD_ID, "extractor");
	public static final ScreenHandlerType<GenericFilterScreenHandler> EXTRACTOR_SCREEN_HANDLER;

	public static final Block EXTRACTOR_UPPER;
	public static final BlockItem EXTRACTOR_UPPER_ITEM;
	public static BlockEntityType<ExtractorUpperEntity> EXTRACTOR_UPPER_ENTITY;
	public static final Identifier EXTRACTOR_UPPER_IDENTIFIER = new Identifier(MOD_ID, "extractor_upper");
	public static final ScreenHandlerType<GenericFilterScreenHandler> EXTRACTOR_UPPER_SCREEN_HANDLER;

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

	public static final Block PULLER;
	public static final BlockItem PULLER_ITEM;
	public static BlockEntityType<PullerEntity> PULLER_ENTITY;
	public static final Identifier PULLER_IDENTIFIER = new Identifier(MOD_ID, "puller");
	public static final ScreenHandlerType<GenericFilterScreenHandler> PULLER_SCREEN_HANDLER;

	public static final Block VACTRACTOR;
	public static final BlockItem VACTRACTOR_ITEM;
	public static BlockEntityType<VactractorEntity> VACTRACTOR_ENTITY;
	public static final Identifier VACTRACTOR_IDENTIFIER = new Identifier(MOD_ID, "vactractor");

	public static final Block INSERTER_UPPER;
	public static final BlockItem INSERTER_UPPER_ITEM;
	public static BlockEntityType<InserterUpperEntity> INSERTER_UPPER_ENTITY;
	public static final Identifier INSERTER_UPPER_IDENTIFIER = new Identifier(MOD_ID, "inserter_upper");
	public static final ScreenHandlerType<GenericFilterScreenHandler> INSERTER_UPPER_SCREEN_HANDLER;

	public static final Block OPI;
	public static final BlockItem OPI_ITEM;
	public static BlockEntityType<OpiEntity> OPI_ENTITY;
	public static final Identifier OPI_IDENTIFIER = new Identifier(MOD_ID, "opi");
	public static final ScreenHandlerType<GenericFilterScreenHandler> OPI_SCREEN_HANDLER;

	// Set the ItemGroup
	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(
					new Identifier(MOD_ID, "general"))
			.icon(() -> new ItemStack(SimpleTransport.CONVEYOR))
			.build();

	static {
		CONVEYOR = Registry.register(Registry.BLOCK, CONVEYOR_IDENTIFIER, new Conveyor(getBlockSettings(CONVEYOR_IDENTIFIER)));
		CONVEYOR_ITEM = Registry.register(Registry.ITEM, CONVEYOR_IDENTIFIER, new BlockItem(CONVEYOR, new FabricItemSettings().group(SimpleTransport.ITEM_GROUP)));
		CONVEYOR_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, CONVEYOR_IDENTIFIER, FabricBlockEntityTypeBuilder.create(ConveyorEntity::new, CONVEYOR).build(null));

		CONVEYOR_DEMAG = Registry.register(Registry.BLOCK, CONVEYOR_DEMAG_IDENTIFIER, new ConveyorDemag(getBlockSettings(CONVEYOR_IDENTIFIER)));
		CONVEYOR_DEMAG_ITEM = Registry.register(Registry.ITEM, CONVEYOR_DEMAG_IDENTIFIER, new BlockItem(CONVEYOR_DEMAG, new FabricItemSettings().group(SimpleTransport.ITEM_GROUP)));
		CONVEYOR_DEMAG_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, CONVEYOR_DEMAG_IDENTIFIER, FabricBlockEntityTypeBuilder.create(ConveyorDemagEntity::new, CONVEYOR_DEMAG).build(null));

		CONVEYOR_DETECTOR = Registry.register(Registry.BLOCK, CONVEYOR_DETECTOR_IDENTIFIER, new ConveyorDetector(getBlockSettings(CONVEYOR_DETECTOR_IDENTIFIER)));
		CONVEYOR_DETECTOR_ITEM = Registry.register(Registry.ITEM, CONVEYOR_DETECTOR_IDENTIFIER, new BlockItem(CONVEYOR_DETECTOR, new FabricItemSettings().group(SimpleTransport.ITEM_GROUP)));
		CONVEYOR_DETECTOR_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, CONVEYOR_DETECTOR_IDENTIFIER, FabricBlockEntityTypeBuilder.create(ConveyorDetectorEntity::new, CONVEYOR_DETECTOR).build(null));

		EXTRACTOR = Registry.register(Registry.BLOCK, EXTRACTOR_IDENTIFIER, new Extractor(getBlockSettings(EXTRACTOR_IDENTIFIER)));
		EXTRACTOR_ITEM = Registry.register(Registry.ITEM, EXTRACTOR_IDENTIFIER, new BlockItem(EXTRACTOR, new FabricItemSettings().group(SimpleTransport.ITEM_GROUP)));
		EXTRACTOR_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, EXTRACTOR_IDENTIFIER, FabricBlockEntityTypeBuilder.create(ExtractorEntity::new, EXTRACTOR).build(null));
		EXTRACTOR_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(EXTRACTOR_IDENTIFIER, GenericFilterScreenHandler::new);

		EXTRACTOR_UPPER = Registry.register(Registry.BLOCK, EXTRACTOR_UPPER_IDENTIFIER, new ExtractorUpper(getBlockSettings(EXTRACTOR_UPPER_IDENTIFIER)));
		EXTRACTOR_UPPER_ITEM = Registry.register(Registry.ITEM, EXTRACTOR_UPPER_IDENTIFIER, new BlockItem(EXTRACTOR_UPPER, new FabricItemSettings().group(SimpleTransport.ITEM_GROUP)));
		EXTRACTOR_UPPER_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, EXTRACTOR_UPPER_IDENTIFIER, FabricBlockEntityTypeBuilder.create(ExtractorUpperEntity::new, EXTRACTOR_UPPER).build(null));
		EXTRACTOR_UPPER_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(EXTRACTOR_UPPER_IDENTIFIER, GenericFilterScreenHandler::new);

		INSERTER = Registry.register(Registry.BLOCK, INSERTER_IDENTIFIER, new Inserter(getBlockSettings(INSERTER_IDENTIFIER)));
		INSERTER_ITEM = Registry.register(Registry.ITEM, INSERTER_IDENTIFIER, new BlockItem(INSERTER, new FabricItemSettings().group(SimpleTransport.ITEM_GROUP)));

		DESTRUCTOR = Registry.register(Registry.BLOCK, DESTRUCTOR_IDENTIFIER, new Destructor(getBlockSettings(DESTRUCTOR_IDENTIFIER)));
		DESTRUCTOR_ITEM = Registry.register(Registry.ITEM, DESTRUCTOR_IDENTIFIER, new BlockItem(DESTRUCTOR, new FabricItemSettings().group(SimpleTransport.ITEM_GROUP)));

		PULLER = Registry.register(Registry.BLOCK, PULLER_IDENTIFIER, new Puller(getBlockSettings(PULLER_IDENTIFIER)));
		PULLER_ITEM = Registry.register(Registry.ITEM, PULLER_IDENTIFIER, new BlockItem(PULLER, new FabricItemSettings().group(SimpleTransport.ITEM_GROUP)));
		PULLER_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, PULLER_IDENTIFIER, FabricBlockEntityTypeBuilder.create(PullerEntity::new, PULLER).build(null));
		PULLER_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(PULLER_IDENTIFIER, GenericFilterScreenHandler::new);

		INSERTER_UPPER = Registry.register(Registry.BLOCK, INSERTER_UPPER_IDENTIFIER, new InserterUpper(getBlockSettings(INSERTER_UPPER_IDENTIFIER)));
		INSERTER_UPPER_ITEM = Registry.register(Registry.ITEM, INSERTER_UPPER_IDENTIFIER, new BlockItem(INSERTER_UPPER, new FabricItemSettings().group(SimpleTransport.ITEM_GROUP)));
		INSERTER_UPPER_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, INSERTER_UPPER_IDENTIFIER, FabricBlockEntityTypeBuilder.create(InserterUpperEntity::new, INSERTER_UPPER).build(null));
		INSERTER_UPPER_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(INSERTER_UPPER_IDENTIFIER, GenericFilterScreenHandler::new);

		OPI = Registry.register(Registry.BLOCK, OPI_IDENTIFIER, new Opi(getBlockSettings(OPI_IDENTIFIER)));
		OPI_ITEM = Registry.register(Registry.ITEM, OPI_IDENTIFIER, new BlockItem(OPI, new FabricItemSettings().group(SimpleTransport.ITEM_GROUP)));
		OPI_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, OPI_IDENTIFIER, FabricBlockEntityTypeBuilder.create(OpiEntity::new, OPI).build(null));
		OPI_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(OPI_IDENTIFIER, GenericFilterScreenHandler::new);

		VACTRACTOR = Registry.register(Registry.BLOCK, VACTRACTOR_IDENTIFIER, new Vactractor(getBlockSettings(VACTRACTOR_IDENTIFIER)));
		VACTRACTOR_ITEM = Registry.register(Registry.ITEM, VACTRACTOR_IDENTIFIER, new BlockItem(VACTRACTOR, new FabricItemSettings().group(SimpleTransport.ITEM_GROUP)));
		VACTRACTOR_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, VACTRACTOR_IDENTIFIER, FabricBlockEntityTypeBuilder.create(VactractorEntity::new, VACTRACTOR).build(null));

	}

	@Override
	public void onInitialize() {

	}

	private static FabricBlockSettings getBlockSettings(Identifier identifier) {
		return FabricBlockSettings.of(Material.STONE).hardness(1.5f).drops(identifier).nonOpaque();
	}
}
