package me.lanzhi.bluestarbot.api;

import me.lanzhi.bluestarbot.BluestarBotPlugin;
import me.lanzhi.bluestarbot.internal.Manager;
import me.lanzhi.bluestarbot.internal.Mapping;
import me.lanzhi.bluestarbot.internal.classloader.MiraiLoader;
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.utils.BotConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static me.lanzhi.bluestarbot.internal.Utils.logger;

/**
 * 一些API的入口
 */
public final class BluestarBot
{
    private static final BluestarBotPlugin plugin=null;

    private BluestarBot()
    {
    }

    /**
     * @return 在线的所以机器人
     */
    public static List<Bot> getBots()
    {
        Thread.currentThread().setContextClassLoader(MiraiLoader.classLoaderAccess().getClassLoader());
        List<Bot> list=new ArrayList<>();
        for (net.mamoe.mirai.Bot bot: net.mamoe.mirai.Bot.getInstances())
        {
            list.add(Mapping.map(bot));
        }
        return list;
    }

    /**
     * 创建机器人,已存在此id的机器人时直接返回,否则尝试登录
     *
     * @param id       qqid
     * @param password 密码
     * @return 创建的实例
     */
    @NotNull
    public static Bot createBot(long id,String password)
    {
        Thread.currentThread().setContextClassLoader(MiraiLoader.classLoaderAccess().getClassLoader());
        Bot bot=getBot(id);
        if (bot!=null)
        {
            return bot;
        }
        BotConfiguration configuration=new BotConfiguration();
        configuration.setLoginSolver(Manager.createLoginSolver());
        configuration.setWorkingDir(new File(JavaPlugin.getPlugin(BluestarBotPlugin.class).getDataFolder(),"bots"));
        configuration.noNetworkLog();
        configuration.noBotLog();
        configuration.randomDeviceInfo();
        configuration.setShowingVerboseEventLog(false);
        configuration.setProtocol(BotConfiguration.MiraiProtocol.IPAD);
        logger().warning("[BluestarBot]预备工作完成,开始新建bot");
        bot=Mapping.map(Mirai.getInstance().getBotFactory().newBot(id,password,configuration));
        logger().warning("[BluestarBot]新建bot完成,开始登录");
        Thread.currentThread().setContextClassLoader(MiraiLoader.classLoaderAccess().getClassLoader());
        bot.login();
        logger().warning("[BluestarBot]登录成功!");
        return bot;
    }

    /**
     * 获取机器人
     *
     * @param id 机器人id
     * @return 找不到时返回null,否则返回机器人
     */
    public static Bot getBot(long id)
    {
        Thread.currentThread().setContextClassLoader(MiraiLoader.classLoaderAccess().getClassLoader());
        return Mapping.map(net.mamoe.mirai.Bot.getInstanceOrNull(id));
    }

    /**
     * 插件提供了我的世界账号和qq账号间的绑定
     *
     * @param uuid 我的世界账号
     * @param id   QQ帐号
     */
    public static void addBind(UUID uuid,long id)
    {
        plugin.getBind().addBind(uuid,id);
    }

    /**
     * 获取绑定
     *
     * @param id qqid
     * @return 我的世界账号
     */
    public static UUID getBind(long id)
    {
        return plugin.getBind().getBind(id);
    }

    /**
     * 获取绑定
     *
     * @param uuid 我的世界
     * @return qq账号
     */
    public static Long getBind(UUID uuid)
    {
        return plugin.getBind().getBind(uuid);
    }

    /**
     * 移除绑定
     *
     * @param uuid 我的世界账号
     * @return 原来绑定的qq账号
     */
    public static Long removeBind(UUID uuid)
    {
        return plugin.getBind().removeBind(uuid);
    }

    /**
     * 移除绑定
     *
     * @param id qq账号
     * @return 原来绑定的Minecraft账号
     */
    public static UUID removeBind(long id)
    {
        return plugin.getBind().removeBind(id);
    }
}
