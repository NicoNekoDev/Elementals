package ro.nicuch.elementals.elementals;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import ro.nicuch.elementals.Elementals;
import ro.nicuch.elementals.User;

//TODO Later, Placehoolder expansion
@SuppressWarnings("deprecation")
public class ElementalsPlaceholders extends EZPlaceholderHook {

    public ElementalsPlaceholders(Elementals plugin) {
        super(plugin, "el");
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null)
            return null;
        User user = Elementals.getUser(player);
        if (identifier.equals("lvl"))
            return String.valueOf(user.getServerLevel());
        if (identifier.equals("points"))
            return String.valueOf(user.getPoints());
        return null;
    }
}
