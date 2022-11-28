package me.lanzhi.bluestarbot.api.event.message.received;

import me.lanzhi.bluestarbot.Mapping;
import me.lanzhi.bluestarbot.api.Group;
import me.lanzhi.bluestarbot.api.NormalGroupMember;
import me.lanzhi.bluestarbot.api.event.GroupMemberEvent;
import me.lanzhi.bluestarbot.api.event.MessageReceivedEvent;

public final class GroupTempMessageEvent extends UserMessageEvent implements MessageReceivedEvent,GroupMemberEvent
{
    public GroupTempMessageEvent(net.mamoe.mirai.event.events.GroupTempMessageEvent event)
    {
        super(event);
    }

    @Override
    public net.mamoe.mirai.event.events.GroupTempMessageEvent getEvent()
    {
        return (net.mamoe.mirai.event.events.GroupTempMessageEvent) super.getEvent();
    }

    @Override
    public NormalGroupMember getContact()
    {
        return Mapping.map(getEvent().getSubject());
    }

    @Override
    public NormalGroupMember getSender()
    {
        return Mapping.map(getEvent().getSender());
    }

    @Override
    public Group getGroup()
    {
        return getSender().getGroup();
    }

    @Override
    public NormalGroupMember getUser()
    {
        return getSender();
    }
}