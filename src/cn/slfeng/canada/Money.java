package cn.slfeng.canada;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;

public class Money implements CommandExecutor {

	private static Economy econ = null;

	public Money(Canada plugin) {
		plugin.getServer().getPluginCommand("ca").setExecutor(this);
	}

	public static Map<String, Integer> money = new HashMap<>();// 玩家押的钱
	public static Map<String, String> jieguo = new HashMap<>();// 玩家赌的结果
	public static Map<String, UUID> uuid = new HashMap<String, UUID>();// 玩家的UUID

	public boolean CheckAndSave(CommandSender sender, String[] args) {
		if (Pattern.compile("^[-\\+]?[\\d]*$").matcher(args[0]).matches() && Integer.parseInt(args[0]) > 0) {// 如果子命令1是整数且大于0
			OfflinePlayer offp = (OfflinePlayer) sender;
			Player onp = (Player) sender;
			if (econ.getBalance(offp) >= Double.parseDouble(args[0])) {// 如果输入的钱数小于存款
				jieguo.put(sender.getName(), args[2]);// 放进数据
				uuid.put(sender.getName(), onp.getUniqueId());// 放入uuid
				econ.withdrawPlayer(onp, Double.parseDouble(args[0]));// 拿走钱款
				sender.sendMessage("您已成功下注，请耐心等待开奖");
			} else {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args[0] == "reload") {
			sender.sendMessage("已重置插件");
		} else if (args[1] == "押") {
			if (args[2] == "大" || args[2] == "小" || args[2] == "单" || args[2] == "双" || args[2] == "大双"
					|| args[2] == "小双" || args[2] == "大单" || args[2] == "小单") {
				CheckAndSave(sender, args);
			} else if (Pattern.compile("^[-\\+]?[\\d]*$").matcher(args[2]).matches() && Integer.parseInt(args[2]) >= 0
					&& Integer.parseInt(args[2]) <= 27) {// 如果是整数且大于等于0小于等于27
				CheckAndSave(sender, args);
			} else {
				return false;
			}
		}
		return true;
	}
}