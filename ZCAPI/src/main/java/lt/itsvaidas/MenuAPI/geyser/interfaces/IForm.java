package lt.itsvaidas.MenuAPI.geyser.interfaces;

import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.Form;
import org.geysermc.floodgate.api.FloodgateApi;

public interface IForm {
    Form build(FloodgateApi api, Player player);
}
