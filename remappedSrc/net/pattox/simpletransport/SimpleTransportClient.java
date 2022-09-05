package net.pattox.simpletransport;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.pattox.simpletransport.gui.GenericFilterScreen;
import net.pattox.simpletransport.init.Conveyorbelts;

public class SimpleTransportClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(Conveyorbelts.PULLER_SCREEN_HANDLER, GenericFilterScreen::new);
        ScreenRegistry.register(Conveyorbelts.EXTRACTOR_SCREEN_HANDLER, GenericFilterScreen::new);
        ScreenRegistry.register(Conveyorbelts.EXTRACTOR_UPPER_SCREEN_HANDLER, GenericFilterScreen::new);
    }
}
