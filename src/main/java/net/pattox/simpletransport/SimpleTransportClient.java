package net.pattox.simpletransport;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.pattox.simpletransport.gui.GenericFilterScreen;

public class SimpleTransportClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(SimpleTransport.PULLER_SCREEN_HANDLER, GenericFilterScreen::new);
        ScreenRegistry.register(SimpleTransport.EXTRACTOR_SCREEN_HANDLER, GenericFilterScreen::new);
        ScreenRegistry.register(SimpleTransport.EXTRACTOR_UPPER_SCREEN_HANDLER, GenericFilterScreen::new);
    }
}
