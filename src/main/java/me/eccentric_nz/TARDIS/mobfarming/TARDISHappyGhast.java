package me.eccentric_nz.TARDIS.mobfarming;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class TARDISHappyGhast extends TARDISMob {

    private ItemStack harness;
    private Location home;
    private TARDISBoat boat;

    public TARDISHappyGhast() {
        super.setType(EntityType.HAPPY_GHAST);
    }

    public ItemStack getHarness() {
        return harness;
    }

    public void setHarness(ItemStack harness) {
        this.harness = harness;
    }

    public Location getHome() {
        return home;
    }

    public void setHome(Location home) {
        this.home = home;
    }

    public TARDISBoat getBoat() {
        return boat;
    }

    public void setBoat(TARDISBoat boat) {
        this.boat = boat;
    }
}
