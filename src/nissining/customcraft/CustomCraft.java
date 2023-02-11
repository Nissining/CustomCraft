package nissining.customcraft;

import cn.nukkit.inventory.CraftingManager;
import cn.nukkit.inventory.FurnaceRecipe;
import cn.nukkit.inventory.ShapedRecipe;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import io.netty.util.collection.CharObjectHashMap;

import java.util.LinkedList;

public class CustomCraft extends PluginBase {

    private Config craftTable;
    private Config furnace;

    @Override
    public void onEnable() {
        if (!getDataFolder().mkdirs()) {
            getLogger().debug("Items Folder is Created!");
        }

        registerCraftTable();
        registerFurnace();
    }

    private Item getItem(String stringItem) {
        // format = "1:0:1"
        String[] ss = stringItem.split(":");
        int id = 0;
        int damage = 0;
        int count = 1;
        if (ss.length == 3) {
            id = Integer.parseInt(ss[0]);
            damage = Integer.parseInt(ss[1]);
            count = Integer.parseInt(ss[2]);
        } else if (ss.length == 2) {
            id = Integer.parseInt(ss[0]);
            damage = Integer.parseInt(ss[1]);
        } else if (ss.length == 1) {
            id = Integer.parseInt(ss[0]);
        }
        return Item.get(id, damage, count);
    }

    private void initCraftConfig() {
        craftTable = new Config(getDataFolder() + "/craftTable.yml", 2, new ConfigSection() {{
            put("key", new ConfigSection() {{
                //有特殊值列子 20:0
                put("sharpLine_1", "0!1!0");
                put("sharpLine_2", "0!1!0");
                put("sharpLine_3", "0!1!0");
                put("output", "20");
            }});
            put("key1", new ConfigSection() {{
                put("sharpLine_1", "0!1!0");
                put("sharpLine_2", "0!1!0");
                put("sharpLine_3", "0!1!0");
                put("output", "20");
            }});
        }});
    }

    private void registerCraftTable() {
        initCraftConfig();

        CraftingManager craftingManager = getServer().getCraftingManager();
        // key, configsection
        craftTable.getRootSection().keySet().forEach(s -> {
            ConfigSection cs = craftTable.getSection(s);

            Item out = getItem(cs.getString("output"));

            String[] line_0 = cs.getString("sharpLine_1").split("!");
            String[] line_1 = cs.getString("sharpLine_2").split("!");
            String[] line_2 = cs.getString("sharpLine_3").split("!");

            String[] sharps = new String[]{"abc", "def", "ghi"};

            CharObjectHashMap<Item> charMap = new CharObjectHashMap<Item>() {{
                put('a', getItem(line_0[0]));
                put('b', getItem(line_0[1]));
                put('c', getItem(line_0[2]));
                put('d', getItem(line_1[0]));
                put('e', getItem(line_1[1]));
                put('f', getItem(line_1[2]));
                put('g', getItem(line_2[0]));
                put('h', getItem(line_2[1]));
                put('i', getItem(line_2[2]));
            }};

            ShapedRecipe shapedRecipe = new ShapedRecipe(out, sharps, charMap, new LinkedList<>())
                    .setIngredient('a', getItem(line_0[0]))
                    .setIngredient('b', getItem(line_0[1]))
                    .setIngredient('c', getItem(line_0[2]))

                    .setIngredient('d', getItem(line_1[0]))
                    .setIngredient('e', getItem(line_1[1]))
                    .setIngredient('f', getItem(line_1[2]))

                    .setIngredient('g', getItem(line_2[0]))
                    .setIngredient('h', getItem(line_2[1]))
                    .setIngredient('i', getItem(line_2[2]));

            // Only Nukkit PM1E
            craftingManager.registerRecipe(419, shapedRecipe);

            getLogger().notice("自定义合成台已创建id: " + s);
        });

        craftingManager.rebuildPacket();
    }

    private void initFurnaceConfig() {
        furnace = new Config(getDataFolder().toString() + "/furnace.yml", 2, new ConfigSection() {{
            put("key", new ConfigSection() {{
                put("input", "1:0");
                put("output", "20:0");
            }});
            put("key1", new ConfigSection() {{
                put("input", "5:0");
                put("output", "20:0");
            }});
        }});
    }

    private void registerFurnace() {
        initFurnaceConfig();
        for (String key : furnace.getAll().keySet()) {
            ConfigSection cs = furnace.getSection(key);
            Item output = getItem(cs.getString("output"));
            FurnaceRecipe furnaceRecipe = new FurnaceRecipe(
                    output,
                    getItem(cs.getString("input"))
            );
            furnaceRecipe.registerToCraftingManager(getServer().getCraftingManager());


//            getLogger().info("自定义熔炉: ID=" + key + " 已注册！");
        }
    }


}
