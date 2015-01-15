package vg.civcraft.mc.namelayer.command.commands;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import vg.civcraft.mc.namelayer.GroupManager.PlayerType;
import vg.civcraft.mc.namelayer.NameAPI;
import vg.civcraft.mc.namelayer.command.PlayerCommand;
import vg.civcraft.mc.namelayer.group.Group;
import vg.civcraft.mc.namelayer.permission.GroupPermission;
import vg.civcraft.mc.namelayer.permission.PermissionType;

public class TransferGroup extends PlayerCommand{

	public TransferGroup(String name) {
		super(name);
		setDescription("This command is used to transfer one group to another person.");
		setUsage("/nlgroupstransfer <group> <player>");
		setIdentifier("nlgroupstransfer");
		setArguments(2,2);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)){
			sender.sendMessage("Nope?");
			return true;
		}
		Player p = (Player) sender;
		Group g = gm.getGroup(args[0]);
		if (g == null){
			p.sendMessage(ChatColor.RED + "This group does not exist.");
			return true;
		}
		
		if (g.isDisciplined()){
			p.sendMessage(ChatColor.RED + "This group is disiplined.");
			return true;
		}
		
		UUID oPlayer = NameAPI.getUUID(args[1]); // uuid of the second player
		UUID uuid = NameAPI.getUUID(p.getName());
		PlayerType pType = g.getPlayerType(uuid);
		if (pType == null){
			p.sendMessage(ChatColor.RED + "You are not a member of this group.");
			return true;
		}
		
		GroupPermission gPerm = gm.getPermissionforGroup(g);
		if (!gPerm.isAccessible(pType, PermissionType.TRANSFER) && 
				!(p.isOp() || p.hasPermission("namelayer.admin"))){
			p.sendMessage(ChatColor.RED + "You do not have permission for this group to transfer it.");
			return true;
		}
		
		g.addMember(oPlayer, PlayerType.OWNER);
		p.sendMessage(ChatColor.GREEN + NameAPI.getCurrentName(oPlayer) + " has been given ownership of the group.");
		return true;
	}

}
