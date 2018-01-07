package cn.slfeng.canada;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import net.milkbowl.vault.economy.Economy;

public class Random {
	private static Economy econ = null;
	private static Canada plugin = Canada.getInstance();
	public static int result;// 上把结果
	public static Map<String, Boolean> property = new HashMap<>();// 结果的属性

	public static int random() { // 产生0-27的随机数
		int random = ((int) (Math.random() * 28)) - 1;
		return random;
	}

	public static void compute() {// 分析结果，大true小false,单true双false,是极大小true不是false
		if (result > 13) {
			property.put("daxiao", true);
			if (result > 21) {
				property.put("jidaxiao", true);
			} else {
				property.put("jidaxiao", false);
			}
		} else {
			property.put("daxiao", false);
			if (result < 6) {
				property.put("jidaxiao", true);
			} else {
				property.put("jidaxiao", false);
			}
		}
		if (result % 2 == 1) {
			property.put("danshuang", true);
		} else {
			property.put("danshuang", false);
		}
	}

	public static String integrate() {
		boolean daxiao = property.get("daxiao");
		boolean danshuang = property.get("danshuang");
		String dx;
		String ds;
		if (daxiao == true) {
			dx = "大";
		} else {
			dx = "小";
		}
		if (danshuang == true) {
			ds = "单";
		} else {
			ds = "双";
		}
		return dx + ds;
	}
	
	public static void Cycle() {// 循环开奖
		plugin.getLogger().info("循环开奖已启动");
		new BukkitRunnable(){
			@Override
		    public void run(){
		    	result = random();// 将random()放进result
				compute();// 执行结果分析
				String jg = integrate();
				Bukkit.broadcastMessage("[加拿大]" + "上把结果已出:" + result);
				if (Money.money == null || Money.money.size() == 0) {
					for (Map.Entry<String, String> entry : Money.jieguo.entrySet()) {
						if (entry.getValue() == jg) {// 如果结果等于上把结果
							if (jg == "大双" || jg == "小双" || jg == "大单" || jg == "小单") {// 如果是组合
								if (Bukkit.getPlayer(entry.getKey()) != null) {// 如果该玩家在线
									econ.depositPlayer(Bukkit.getPlayer(entry.getKey()),
											Money.money.get(entry.getKey()) * 4.2);
								} else {// 如果不在线
									econ.depositPlayer(Bukkit.getOfflinePlayer(Money.uuid.get(entry.getKey())),
											Money.money.get(entry.getKey()) * 4.2);
								}
							} else if (jg == "大" || jg == "小" || jg == "单" || jg == "双") {
								if (Bukkit.getPlayer(entry.getKey()) != null) {// 如果该玩家在线
									econ.depositPlayer(Bukkit.getPlayer(entry.getKey()),
											Money.money.get(entry.getKey()) * 2);
								} else {// 如果不在线
									econ.depositPlayer(Bukkit.getOfflinePlayer(Money.uuid.get(entry.getKey())),
											Money.money.get(entry.getKey()) * 2);
								}
							} else {// 如果是赌数字
								if (Bukkit.getPlayer(entry.getKey()) != null) {// 如果该玩家在线
									econ.depositPlayer(Bukkit.getPlayer(entry.getKey()),
											Money.money.get(entry.getKey()) * 12);
								} else {// 如果不在线
									econ.depositPlayer(Bukkit.getOfflinePlayer(Money.uuid.get(entry.getKey())),
											Money.money.get(entry.getKey()) * 12);
								}
							}
						} else {
							if (Bukkit.getPlayer(entry.getKey()) != null) {// 如果该玩家在线
								Bukkit.getPlayer(entry.getKey())
										.sendMessage("您上把赌的结果是：" + entry.getValue() + "很遗憾您没有中奖");
							}
						}
					}
				} else {
					Bukkit.broadcastMessage("上把无人下注");
				}
		    }
		}.runTaskTimerAsynchronously(plugin, 30*20L, 180*20L);
	}
}