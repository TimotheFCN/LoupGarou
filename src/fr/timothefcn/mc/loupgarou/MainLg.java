package fr.timothefcn.mc.loupgarou;


import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.google.common.collect.HashBiMap;
import fr.timothefcn.mc.com.comphenix.packetwrapper.*;
import fr.timothefcn.mc.loupgarou.classes.LGGame;
import fr.timothefcn.mc.loupgarou.classes.LGPlayer;
import fr.timothefcn.mc.loupgarou.classes.LGWinType;
import fr.timothefcn.mc.loupgarou.events.LGSkinLoadEvent;
import fr.timothefcn.mc.loupgarou.events.LGUpdatePrefixEvent;
import fr.timothefcn.mc.loupgarou.listeners.*;
import fr.timothefcn.mc.loupgarou.roles.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static fr.timothefcn.mc.loupgarou.classes.LGPlayer.thePlayer;

public class MainLg extends JavaPlugin {
    private static MainLg instance;
    @Getter
    private static String prefix = ""/*"§7[§9Loup-Garou§7] "*/;
    @Getter
    private HashMap<String, Constructor<? extends Role>> roles = new HashMap<>();
    @Getter
    @Setter
    private LGGame currentGame;//Because for now, only one game will be playable on one server (flemme)
    @Getter
    private HashBiMap<String, LGGame> allGames = HashBiMap.create();
    @Getter
    private ArrayList<String> badGuys = new ArrayList<>();
    @Getter
    private ArrayList<Player> bypass = new ArrayList<>();
    @Getter
    private HashMap<String, Integer> rolesIconsID = new HashMap<>();

    public static MainLg getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        loadRoles();
        setBadGuys();
        setRoleIconID();
        if (!new File(getDataFolder(), "config.yml").exists()) {//Créer la config
            FileConfiguration config = getConfig();
            config.set("spawns", new ArrayList<List<Double>>());
            config.set("ressourcePack", "http://redoubler.fun/ressources/loup_garou.zip");
            saveConfig();
        }
        // loadConfig();
        Bukkit.getConsoleSender().sendMessage("LG by Timothé");
        getCommand("create").setExecutor(new LgCommands());
        getCommand("join").setExecutor(new LgCommands());
        getCommand("bypass").setExecutor(new LgCommands());
        getCommand("leave").setExecutor(new LgCommands());
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new CancelListener(), this);
        Bukkit.getPluginManager().registerEvents(new VoteListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new LoupGarouListener(), this);
        Bukkit.getPluginManager().registerEvents(new RoleInterfaceListener(), this);

        for (Player player : Bukkit.getOnlinePlayers())
            Bukkit.getPluginManager().callEvent(new PlayerJoinEvent(player, "is connected"));

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.UPDATE_TIME) {
                                              @Override
                                              public void onPacketSending(PacketEvent event) {
                                                  WrapperPlayServerUpdateTime time = new WrapperPlayServerUpdateTime(event.getPacket());
                                                  LGPlayer lgp = thePlayer(event.getPlayer());
                                                  if (lgp.getGame() != null && lgp.getGame().getTime() != time.getTimeOfDay())
                                                      event.setCancelled(true);
                                              }
                                          }
        );
        //Éviter que les gens s'entendent quand ils se sélectionnent et qu'ils sont trop proche
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.NAMED_SOUND_EFFECT) {
                                              @Override
                                              public void onPacketSending(PacketEvent event) {
                                                  WrapperPlayServerNamedSoundEffect sound = new WrapperPlayServerNamedSoundEffect(event.getPacket());
                                                  if (sound.getSoundEffect() == Sound.ENTITY_PLAYER_ATTACK_NODAMAGE)
                                                      event.setCancelled(true);
                                              }
                                          }
        );
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.PLAYER_INFO) {
            @Override
            public void onPacketSending(PacketEvent event) {
                LGPlayer player = thePlayer(event.getPlayer());
                WrapperPlayServerPlayerInfo info = new WrapperPlayServerPlayerInfo(event.getPacket());
                ArrayList<PlayerInfoData> datas = new ArrayList<>();
                for (PlayerInfoData data : info.getData()) {
                    LGPlayer lgp = thePlayer(Bukkit.getPlayer(data.getProfile().getUUID()));
                    if (player.getGame() != null && player.getGame() == lgp.getGame()) {
                        LGUpdatePrefixEvent evt2 = new LGUpdatePrefixEvent(player.getGame(), lgp, player, "");
                        WrappedChatComponent displayName = data.getDisplayName();
                        Bukkit.getPluginManager().callEvent(evt2);
                        if (evt2.getPrefix().length() > 0) {
                            try {
                                if (displayName != null) {
                                    JSONObject obj = (JSONObject) new JSONParser().parse(displayName.getJson());
                                    displayName = WrappedChatComponent.fromText(evt2.getPrefix() + obj.get("text"));
                                } else
                                    displayName = WrappedChatComponent.fromText(evt2.getPrefix() + data.getProfile().getName());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        LGSkinLoadEvent evt = new LGSkinLoadEvent(lgp.getGame(), lgp, player, data.getProfile());
                        Bukkit.getPluginManager().callEvent(evt);
                        datas.add(new PlayerInfoData(evt.getProfile(), data.getLatency(), data.getGameMode(), displayName));
                    } else
                        datas.add(data);
                }
                info.setData(datas);
            }
        });
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.UPDATE_HEALTH) {
            @Override
            public void onPacketSending(PacketEvent event) {
                LGPlayer player = thePlayer(event.getPlayer());
                if (player.getGame() != null && player.getGame().isStarted()) {
                    WrapperPlayServerUpdateHealth health = new WrapperPlayServerUpdateHealth(event.getPacket());
                    health.setFood(6);
                }
            }
        });
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.SCOREBOARD_TEAM) {
            @Override
            public void onPacketSending(PacketEvent event) {
                LGPlayer player = thePlayer(event.getPlayer());
                WrapperPlayServerScoreboardTeam team = new WrapperPlayServerScoreboardTeam(event.getPacket());
                team.setColor(ChatColor.WHITE);
                Player other = Bukkit.getPlayer(team.getName());
                if (other == null) return;
                LGPlayer lgp = thePlayer(other);
                if (player.getGame() != null && player.getGame() == lgp.getGame()) {
                    LGUpdatePrefixEvent evt2 = new LGUpdatePrefixEvent(player.getGame(), lgp, player, "");
                    Bukkit.getPluginManager().callEvent(evt2);
                    if (evt2.getPrefix().length() > 0)
                        team.setPrefix(WrappedChatComponent.fromText(evt2.getPrefix()));
                    else
                        team.setPrefix(WrappedChatComponent.fromText("§f"));
                }
            }
        });
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_EQUIPMENT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                LGPlayer player = thePlayer(event.getPlayer());
                if (player.getGame() != null) {
                    WrapperPlayServerEntityEquipment equip = new WrapperPlayServerEntityEquipment(event.getPacket());
                    if (equip.getSlot() == ItemSlot.OFFHAND && equip.getEntityID() != player.getPlayer().getEntityId())
                        equip.setItem(new ItemStack(Material.AIR));
                }
            }
        });

    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("lg")) {
            if (!sender.hasPermission("loupgarou.admin")) {
                sender.sendMessage(prefix + "§4Erreur: Vous n'avez pas la permission...");
                return true;
            }
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("addspawn")) {
                    Player player = (Player) sender;
                    Location loc = player.getLocation();
                    List<Object> list = (List<Object>) getConfig().getList("spawns");
                    list.add(Arrays.asList((double) loc.getBlockX(), loc.getY(), (double) loc.getBlockZ(), (double) loc.getYaw(), (double) loc.getPitch()));
                    saveConfig();
                    //   loadConfig();
                    sender.sendMessage(prefix + "§aLa position a bien été ajoutée !");
                    return true;
                } else if (args[0].equalsIgnoreCase("end")) {
                    if (args.length != 2) {
                        sender.sendMessage("§4Utilisation : §c/lg end <pseudo>");
                        return true;
                    }
                    Player selected = Bukkit.getPlayer(args[1]);
                    if (selected == null) {
                        sender.sendMessage("§4Erreur : §cLe joueur §4" + args[1] + "§c n'est pas connecté.");
                        return true;
                    }
                    LGGame game = thePlayer(selected).getGame();
                    if (game == null) {
                        sender.sendMessage("§4Erreur : §cLe joueur §4" + selected.getName() + "§c n'est pas dans une partie.");
                        return true;
                    }
                    game.cancelWait();
                    game.endGame(LGWinType.EQUAL);
                    game.broadcastMessage("§cLa partie a été arrêtée de force !");
                    return true;
                } else if (args[0].equalsIgnoreCase("start")) {
                    if (args.length < 2) {
                        sender.sendMessage("§4Utilisation : §c/lg start <pseudo>");
                        return true;
                    }
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null) {
                        sender.sendMessage("§4Erreur : §cLe joueur §4" + args[1] + "§c n'existe pas !");
                        return true;
                    }
                    LGPlayer lgp = thePlayer(player);
                    if (lgp.getGame() == null) {
                        sender.sendMessage("§4Erreur : §cLe joueur §4" + lgp.getName() + "§c n'est pas dans une partie.");
                        return true;
                    }
                    if (MainLg.getInstance().getConfig().getList("spawns").size() < lgp.getGame().getMaxPlayers()) {
                        sender.sendMessage("§4Erreur : §cIl n'y a pas assez de points de spawn !");
                        sender.sendMessage("§8§oPour les définir, merci de faire §7/lg addSpawn");
                        return true;
                    }
                    sender.sendMessage("§aVous avez bien démarré une nouvelle partie !");
                    lgp.getGame().updateStart();
                    return true;
                } else if (args[0].equalsIgnoreCase("reloadconfig")) {
                    sender.sendMessage("§aVous avez bien reload la config !");
                    sender.sendMessage("§7§oSi vous avez changé les rôles, écriver §8§o/lg joinall§7§o !");
                    //   loadConfig();
                    return true;

                /*} else if (args[0].equalsIgnoreCase("joinall")) {
                    for (Player p : Bukkit.getOnlinePlayers())
                        Bukkit.getPluginManager().callEvent(new PlayerQuitEvent(p, "joinall"));
                    for (Player p : Bukkit.getOnlinePlayers())
                        Bukkit.getPluginManager().callEvent(new PlayerJoinEvent(p, "joinall"));
                    return true; */
                } else if (args[0].equalsIgnoreCase("reloadPacks")) {
                    for (Player p : Bukkit.getOnlinePlayers())
                        Bukkit.getPluginManager().callEvent(new PlayerQuitEvent(p, "reloadPacks"));
                    for (Player p : Bukkit.getOnlinePlayers())
                        Bukkit.getPluginManager().callEvent(new PlayerJoinEvent(p, "reloadPacks"));
                    return true;
                } else if (args[0].equalsIgnoreCase("nextNight")) {
                    sender.sendMessage("§aVous êtes passé à la prochaine nuit");
                    if (sender instanceof Player && thePlayer((Player) sender).getGame() != null) {
                        LGGame playergame = LGPlayer.thePlayer((Player) sender).getGame();
                        playergame.broadcastMessage("§2§lLe passage à la prochaine nuit a été forcé !");
                        for (LGPlayer lgp : playergame.getInGame())
                            lgp.stopChoosing();
                        playergame.cancelWait();
                        playergame.nextNight();
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("nextDay")) {
                    sender.sendMessage("§aVous êtes passé à la prochaine journée");
                    if (sender instanceof Player && thePlayer((Player) sender).getGame() != null) {
                        LGGame playergame = LGPlayer.thePlayer((Player) sender).getGame();
                        playergame.broadcastMessage("§2§lLe passage à la prochaine journée a été forcé !");
                        playergame.cancelWait();
                        for (LGPlayer lgp : playergame.getInGame())
                            lgp.stopChoosing();
                        playergame.endNight();
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("roles")) {
                    if (args.length == 1 || args[1].equalsIgnoreCase("list")) {
                        sender.sendMessage(prefix + "§6Voici la liste des rôles:");
                        int index = 0;
                        for (String role : getRoles().keySet())
                            sender.sendMessage(prefix + "  §e- " + index++ + " - §6" + role + " §e> " + MainLg.getInstance().getConfig().getInt("role." + role));
                        sender.sendMessage("\n" + prefix + " §7Écrivez §8§o/lg roles set <role_id/role_name> <nombre>§7 pour définir le nombre de joueurs qui devrons avoir ce rôle.");
                    } else {
                        if (args[1].equalsIgnoreCase("set") && args.length == 4) {
                            String role = null;
                            if (args[2].length() <= 2)
                                try {
                                    Integer i = Integer.valueOf(args[2]);
                                    Object[] array = getRoles().keySet().toArray();
                                    if (array.length <= i) {
                                        sender.sendMessage(prefix + "§4Erreur: §cCe rôle n'existe pas.");
                                        return true;
                                    } else
                                        role = (String) array[i];
                                } catch (Exception err) {
                                    sender.sendMessage(prefix + "§4Erreur: §cCeci n'est pas un nombre");
                                }
                            else
                                role = args[2];

                            if (role != null) {
                                String real_role = null;
                                for (String real : getRoles().keySet())
                                    if (real.equalsIgnoreCase(role)) {
                                        real_role = real;
                                        break;
                                    }

                                if (real_role != null) {
                                    try {
                                        MainLg.getInstance().getConfig().set("role." + real_role, Integer.valueOf(args[3]));
                                        sender.sendMessage(prefix + "§6Il y aura §e" + args[3] + " §6" + real_role);
                                        saveConfig();
                                        //    loadConfig();
                                        sender.sendMessage("§7§oSi vous avez fini de changer les rôles, écriver §8§o/lg joinall§7§o !");
                                    } catch (Exception err) {
                                        sender.sendMessage(prefix + "§4Erreur: §c" + args[3] + " n'est pas un nombre");
                                    }
                                    return true;
                                }
                            }
                            sender.sendMessage(prefix + "§4Erreur: §cLe rôle que vous avez entré est incorrect");

                        } else {
                            sender.sendMessage(prefix + "§4Erreur: §cCommande incorrecte.");
                            sender.sendMessage(prefix + "§4Essayez §c/lg roles set <role_id/role_name> <nombre>§4 ou §c/lg roles list");
                        }
                    }
                    return true;
                }
            }
            sender.sendMessage(prefix + "§4Erreur: §cCommande incorrecte.");
            sender.sendMessage(prefix + "§4Essayez /lg §caddSpawn/end/start/nextNight/nextDay/reloadConfig/roles/reloadPacks/joinAll");
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("loupgarou.admin"))
            return new ArrayList<String>(0);

        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("roles"))
                if (args.length == 2)
                    return getStartingList(args[1], "list", "set");
                else if (args.length == 3 && args[1].equalsIgnoreCase("set"))
                    return getStartingList(args[2], getRoles().keySet().toArray(new String[getRoles().size()]));
                else if (args.length == 4)
                    return Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        } else if (args.length == 1)
            return getStartingList(args[0], "addSpawn", "end", "start", "nextNight", "nextDay", "reloadConfig", "roles", "joinAll", "reloadPacks");
        return new ArrayList<String>(0);
    }

    private List<String> getStartingList(String startsWith, String... list) {
        startsWith = startsWith.toLowerCase();
        ArrayList<String> returnlist = new ArrayList<>();
        if (startsWith.length() == 0)
            return Arrays.asList(list);
        for (String s : list)
            if (s.toLowerCase().startsWith(startsWith))
                returnlist.add(s);
        return returnlist;
    }

 /*   public void loadConfig() {
        int players = 0;
        for (String role : roles.keySet())
            players += getConfig().getInt("role." + role);
        currentGame = new LGGame(players);
    } */

    @Override
    public void onDisable() {
        ProtocolLibrary.getProtocolManager().removePacketListeners(this);
    }

    private void loadRoles() {
        try {
            roles.put("LoupGarou", RLoupGarou.class.getConstructor(LGGame.class));
            roles.put("LoupGarouNoir", RLoupGarouNoir.class.getConstructor(LGGame.class));
            roles.put("Garde", RGarde.class.getConstructor(LGGame.class));
            roles.put("Sorciere", RSorciere.class.getConstructor(LGGame.class));
            roles.put("Voyante", RVoyante.class.getConstructor(LGGame.class));
            roles.put("Chasseur", RChasseur.class.getConstructor(LGGame.class));
            roles.put("Villageois", RVillageois.class.getConstructor(LGGame.class));
            roles.put("Medium", RMedium.class.getConstructor(LGGame.class));
            roles.put("Dictateur", RDictateur.class.getConstructor(LGGame.class));
            roles.put("Cupidon", RCupidon.class.getConstructor(LGGame.class));
            roles.put("PetiteFille", RPetiteFille.class.getConstructor(LGGame.class));
            roles.put("ChaperonRouge", RChaperonRouge.class.getConstructor(LGGame.class));
            roles.put("LoupGarouBlanc", RLoupGarouBlanc.class.getConstructor(LGGame.class));
            roles.put("Bouffon", RBouffon.class.getConstructor(LGGame.class));
            roles.put("Ange", RAnge.class.getConstructor(LGGame.class));
            roles.put("Survivant", RSurvivant.class.getConstructor(LGGame.class));
            roles.put("Assassin", RAssassin.class.getConstructor(LGGame.class));
            roles.put("GrandMechantLoup", RGrandMechantLoup.class.getConstructor(LGGame.class));
            roles.put("Corbeau", RCorbeau.class.getConstructor(LGGame.class));
            roles.put("Detective", RDetective.class.getConstructor(LGGame.class));
            roles.put("ChienLoup", RChienLoup.class.getConstructor(LGGame.class));
            roles.put("Pirate", RPirate.class.getConstructor(LGGame.class));
            roles.put("Pyromane", RPyromane.class.getConstructor(LGGame.class));
            roles.put("Pretre", RPretre.class.getConstructor(LGGame.class));
            roles.put("Faucheur", RFaucheur.class.getConstructor(LGGame.class));
            roles.put("EnfantSauvage", REnfantSauvage.class.getConstructor(LGGame.class));
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

    }

    private void setBadGuys() {
        badGuys.add(("LoupGarou"));
        badGuys.add(("LoupGarouNoir"));
        badGuys.add(("LoupGarouBlanc"));
        badGuys.add(("GrandMechantLoup"));
        badGuys.add(("ChienLoup")); //TODO: implémenter ça correctement
        badGuys.add(("EnfantSauvage")); //TODO: implémenter ça correctement
    }

    private void setRoleIconID() {
        rolesIconsID.put("LoupGarou", 1017);
        rolesIconsID.put("LoupGarouNoir", 1019);
        rolesIconsID.put("Garde", 1015);
        rolesIconsID.put("Sorciere", 1026);
        rolesIconsID.put("Voyante", 1030);
        rolesIconsID.put("Chasseur", 1006);
        rolesIconsID.put("Villageois", 1029);
        rolesIconsID.put("Medium", 1020);
        rolesIconsID.put("Dictateur", 1012);
        rolesIconsID.put("Cupidon", 1010);
        rolesIconsID.put("PetiteFille", 1022);
        rolesIconsID.put("ChaperonRouge", 1005);
        rolesIconsID.put("LoupGarouBlanc", 1018);
        rolesIconsID.put("Bouffon", 1003);
        rolesIconsID.put("Ange", 1001);
        rolesIconsID.put("Survivant", 1027);
        rolesIconsID.put("Assassin", 1002);
        rolesIconsID.put("GrandMechantLoup", 1016);
        rolesIconsID.put("Corbeau", 1009);
        rolesIconsID.put("Detective", 1011);
        rolesIconsID.put("ChienLoup", 1008);
        rolesIconsID.put("Pirate", 1023);
        rolesIconsID.put("Pyromane", 1025);
        rolesIconsID.put("Pretre", 1024);
        rolesIconsID.put("Faucheur", 1014);
        rolesIconsID.put("EnfantSauvage", 1013);
    }
}
