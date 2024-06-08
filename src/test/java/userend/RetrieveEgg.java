package userend;

import be.raft.pelican.PelicanBuilder;
import be.raft.pelican.application.entities.ApplicationEgg;
import be.raft.pelican.entities.PelicanApi;

public class RetrieveEgg {
    private static final PelicanApi api = PelicanBuilder.create(System.getenv("plc_url"), System.getenv("plc_token"))
            .create();

    public static void main(String[] args) {
        ApplicationEgg egg = api.application().retrieveEggById(1)
                .onErrorMap(cause -> {
                    System.err.println("Failed to fetch egg: " + cause);
                    return null;
                })
                .execute();

        System.out.println("Retrieved Egg: " + egg);
    }
}
