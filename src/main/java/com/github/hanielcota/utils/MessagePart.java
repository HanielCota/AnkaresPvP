package com.github.hanielcota.utils;

import lombok.Getter;
import net.md_5.bungee.api.chat.*;

@Getter
public class MessagePart {

    private final TextComponent component;

    public MessagePart(TextComponent component) {
        this.component = component;
    }

    public MessagePart(String text) {
        this(new TextComponent(text));
    }

    public MessagePart() {
        this(new TextComponent());
    }

    public void text(String text) {
        component.setText(text);
    }

    public void hover(String message) {
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(message).create()));
    }

    public void click(ClickEvent.Action action, String value) {
        component.setClickEvent(new ClickEvent(action, value));
    }

    public void command(String value) {
        click(ClickEvent.Action.RUN_COMMAND, value);
    }

    public void extra(BaseComponent component) {
        this.component.addExtra(component);
    }
}