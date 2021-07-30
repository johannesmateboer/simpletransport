package net.pattox.simpletransport;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.pattox.simpletransport.block.Conveyor;

public class SimpleTransport implements ModInitializer {

	public static final String MOD_ID = "simpletransport";

	public static final Block CONVEYOR;
	public static final BlockItem CONVEYOR_ITEM;
	public static final Identifier CONVEYOR_IDENTIFIER = new Identifier(MOD_ID, "st_conveyor");

	// Set the ItemGroup
	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(
					new Identifier(MOD_ID, "general"))
			.icon(() -> new ItemStack(Blocks.CHEST))
			.build();

	static {
		CONVEYOR = Registry.register(Registry.BLOCK, CONVEYOR_IDENTIFIER, new Conveyor(FabricBlockSettings.copyOf(Blocks.ACACIA_WOOD).nonOpaque()));
		CONVEYOR_ITEM = Registry.register(Registry.ITEM, CONVEYOR_IDENTIFIER, new BlockItem(CONVEYOR, new FabricItemSettings().group(SimpleTransport.ITEM_GROUP)));
	}

	@Override
	public void onInitialize() {




	}
}
