package cc.datta.twitchdevtool.games;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Consumer;

public class TntGameDonations {
    public int bits;
    public String name;
    public Consumer<Player> run;
    public Material material;

    public TntGameDonations(Material material, int bits, String name, Consumer<Player> run) {
        this.material = material;
        this.bits = bits;
        this.name = name;
        this.run = run;
    }

    public void execute(Player player) {
        this.run.accept(player);
    }

    public int getBits() {
        return this.bits;
    }

    public String getName() {
        return this.name;
    }

    public Consumer<Player> getRun() {
        return this.run;
    }

    public Material getMaterial() {
        return this.material;
    }
}
