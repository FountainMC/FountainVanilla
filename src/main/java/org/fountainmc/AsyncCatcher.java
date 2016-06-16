package org.fountainmc;

import net.minecraft.server.MinecraftServer;

import static com.google.common.base.Preconditions.checkNotNull;

public class AsyncCatcher {

    private AsyncCatcher() {
    }

    public static void checkAsyncOp(String reason) {
        if (Thread.currentThread() != MinecraftServer.getDedicatedServer().getServerThread()) {
            throw new IllegalStateException(illegalAsyncOpMessage(reason));
        }
    }

    private static String illegalAsyncOpMessage(String reason) {
        return "A plugin tried to " + checkNotNull(reason, "Null reason") + " asynchronously!";
    }

}
