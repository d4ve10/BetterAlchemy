package de.d4ve10.betteralchemy.listeners;

import de.d4ve10.betteralchemy.BetterAlchemy;
import de.d4ve10.betteralchemy.potion.CustomPotion;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CauldronListener implements Listener {

    private final Map<Block, CustomPotion> cauldronPotions = new HashMap<>();
    private final Map<Block, Integer> potionAmounts = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || event.getItem() == null) {
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK
                && event.getItem().getType() == Material.STICK
                && event.getClickedBlock().getType() == Material.WATER_CAULDRON) {

            Material heatSource = event.getClickedBlock().getRelative(BlockFace.DOWN).getType();
            if (heatSource.equals(Material.FIRE) || heatSource.equals(Material.LAVA) || heatSource.equals(Material.CAMPFIRE)) {
                Collection<Entity> cItems = event.getClickedBlock().getWorld().getNearbyEntities(event.getClickedBlock().getLocation(), 1, 1, 1);
                ArrayList<ItemStack> CauldronItems = new ArrayList<>();


                for (Entity cItem : cItems) {
                    if (!(cItem instanceof Item))
                        continue;
                    Item item = (Item) cItem;
                    if (item.getLocation().getBlock().getType() == Material.WATER_CAULDRON)
                        CauldronItems.add(item.getItemStack());
                }

                CustomPotion potion = BetterAlchemy.getInstance().getPotionMaker().createPotion(CauldronItems);
                if (potion != null) {
                    cauldronPotions.put(event.getClickedBlock(), potion);
                    potionAmounts.put(event.getClickedBlock(), 0);
                    potion.spawnParticles(event.getClickedBlock().getLocation().add(0.5, 1.5, 0.5), 75);
                    for (Entity entity : event.getClickedBlock().getWorld().getNearbyEntities(event.getClickedBlock().getLocation(), 10, 10, 10)) {
                        if (entity instanceof Player) {
                            Player player = (Player) entity;
                            player.playSound(event.getClickedBlock().getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1, 1);
                        }
                    }
                }

            }
        } else if (event.getClickedBlock().getType().equals(Material.WATER_CAULDRON) && event.getMaterial() == Material.GLASS_BOTTLE) {
            ItemStack item = event.getItem();
            Player player = event.getPlayer();
            Block clickedBlock = event.getClickedBlock();

            if (player.getInventory().firstEmpty() != -1 || item.getAmount() == 1) {
                if (cauldronPotions.containsKey(event.getClickedBlock()) && potionAmounts.containsKey(event.getClickedBlock())) {
                    CustomPotion potion = cauldronPotions.get(clickedBlock);
                    int potionAmount = potionAmounts.get(clickedBlock);
                    if (potionAmount >= 3) {
                        cauldronPotions.remove(clickedBlock);
                        potionAmounts.remove(clickedBlock);
                        return;
                    }
                    ItemStack potionItem = potion.create();
                    BlockData data = clickedBlock.getBlockData();
                    if (!(data instanceof Levelled))
                        return;
                    Levelled cauldron = (Levelled) data;

                    event.setCancelled(true);
                    if (cauldron.getLevel() == 1)
                        clickedBlock.setType(Material.CAULDRON);
                    else {
                        cauldron.setLevel(cauldron.getLevel() - 1);
                        clickedBlock.setBlockData(cauldron);
                    }
                    if (item.getAmount() > 1)
                        item.setAmount(item.getAmount() - 1);
                    else {
                        if ((event.getHand() == EquipmentSlot.OFF_HAND))
                            event.getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                        else
                            event.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    }

                    BetterAlchemy.getInstance().getServer().getScheduler().runTaskLater(BetterAlchemy.getInstance(), () -> {
                        clickedBlock.getWorld().playSound(clickedBlock.getLocation(), Sound.ITEM_BOTTLE_FILL, 1f, 1f);
                        if (event.getHand() == EquipmentSlot.OFF_HAND && player.getInventory().getItemInOffHand().getType() == Material.AIR)
                            player.getInventory().setItemInOffHand(potionItem);
                        else
                            player.getInventory().addItem(potionItem);
                        potionAmounts.put(clickedBlock, potionAmount + 1);
                    }, 1L);
                }
            } else {
                event.setCancelled(true);
            }
        }
    }

}
