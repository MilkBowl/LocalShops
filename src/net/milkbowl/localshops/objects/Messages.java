package net.milkbowl.localshops.objects;

public enum Messages {
	BASE_SHOP("Base.Shop"),
	BASE_CHAT_PREFIX("Base.ChatPrefix"),
	BASE_TRUE("Base.True"),
	BASE_FALSE("Base.False"),
	
	MAIN_LOAD("Main.Load"),
	MAIN_ERROR_LOAD_LOCALE("Main.ErrorLoadingLocale"),
	MAIN_USING_LOCALE("Main.UsingLocale"),
	MAIN_ENABLE("Main.Enable"),
	MAIN_DISABLE("Main.Disable"),
	MAIN_ECONOMY_NOT_FOUND("Main.EconomyNotFound"),
	MAIN_PERMISSION_NOT_FOUND("Main.PermissionNotFound"),
	
	GEN_INVALID_VALUE("Generic.InvalidValue"),
	GEN_USER_ACCESS_DENIED("Generic.UserAccessDenied"),
	GEN_NOT_IN_SHOP("Generic.UserNotInShop"),
	GEN_MUST_BE_SHOP_OWNER("Generic.UserMustBeShopOwner"),
	GEN_MUST_BE_OWN_OR_MAN("Generic.UserMustBeOwnOrMan"),
	GEN_CURR_OWNER_IS("Generic.CurrentOwnerIs"),
	GEN_ITEM_NOT_FOUND("Generic.ItemNotFound"),
	GEN_CONSOLE_NOT_IMPLEMENTED("Generic.ConsoleNotImplemented"),
	GEN_UNEXPECTED_MONEY_ISSUE("Generic.UnexpectedMoneyIssue"),
	GEN_ITEM_NOT_CARRIED("Generic.ItemNotCarried"),
	GEN_SHOP_NOT_FOUND("Generic.ShopNotFound"),
	
	CMD_ISSUED_LOCAL("Command.IssuedLocal"),
	CMD_ISSUED_GLOBAL("Command.IssuedGlobal"),
	
	CMD_ADM_SET_CFG_CHARGE_FOR_SHOP("Command.Admin.SetConfig.ChargeForShop"),
	CMD_ADM_SET_CFG_GLOBAL_SHOP("Command.Admin.SetConfig.GlobalShop"),
	CMD_ADM_SET_CFG_SHOP_WIDTH("Command.Admin.SetConfig.ShopWidth"),
	CMD_ADM_SET_CFG_GLOBAL_STOCK("Command.Admin.SetConfig.GlobalShopStock"),
	CMD_ADM_SET_CFG_REPORT_STATS("Command.Admin.SetConfig.ReportStats"),
	CMD_ADM_SET_CFG_MAX_HEIGHT("Command.Admin.SetConfig.MaxHeight"),
	CMD_ADM_SET_CFG_MAX_WIDTH("Command.Admin.SetConfig.MaxWidth"),
	CMD_ADM_SET_CFG_SHOPS_TRANS_MAX_SIZE("Command.Admin.SetConfig.ShopsTransactionsMaxSize"),
	CMD_ADM_SET_CFG_SHOPS_COST("Command.Admin.SetConfig.ShopsCost"),
	CMD_ADM_SET_CFG_FIND_MAX_DISTANCE("Command.Admin.SetConfig.FindMaxDistance"),
	CMD_ADM_SET_CFG_SHOPS_PER_PLAYER("Command.Admin.SetConfig.ShopsPerPlayer"),
	CMD_ADM_SET_CFG_SHOP_HEIGHT("Command.Admin.SetConfig.ShopHeight"),
	CMD_ADM_SET_CFG_DEBUG("Command.Admin.SetConfig.Debug"),
	CMD_ADM_SET_CFG_MAX_DAMAGE("Command.Admin.SetConfig.MaxDamage"),
	CMD_ADM_SET_CFG_MOVE_COST("Command.Admin.SetConfig.MoveCost"),
	CMD_ADM_SET_CFG_SHOP_NOTIFICATION_TIMER("Command.Admin.SetConfig.ShopNotificationTimer"),
	CMD_ADM_SET_CFG_SHOP_NOTIFICATION("Command.Admin.SetConfig.ShopNotification"),
	CMD_ADM_SET_CFG_CHAT_MAX_LINES("Command.Admin.SetConfig.ChatMaxLines"),
	CMD_ADM_SET_CFG_LOG_TRANSACTIONS("Command.Admin.SetConfig.LogTransactions"),
	
	CMD_SHP_ADD_TOO_DAM("Command.Shop.Add.TooDamaged"),
	CMD_SHP_ADD_DMG_LESS_THAN("Command.Shop.Add.DamageLessThan"),
	CMD_SHP_ADD_USAGE("Command.Shop.Add.Usage"),
	CMD_SHP_ADD_LOG("Command.Shop.Add.Log"),
	CMD_SHP_ADD_INSUFFICIENT_INV("Command.Shop.Add.InsufficientInventory"),
	CMD_SHP_ADD_UNLIM_STOCK("Command.Shop.Add.UnlimitedStock"),
	CMD_SHP_ADD_ALREADY_HAS("Command.Shop.Add.AlreadyHas"),
	CMD_SHP_ADD_SUCCESS("Command.Shop.Add.Success"),
	CMD_SHP_ADD_STOCK_SUCCESS("Command.Shop.Add.SuccessWithStock"),
	CMD_SHP_ADD_READY0("Command.Shop.Add.Ready0"),
	CMD_SHP_ADD_READY1("Command.Shop.Add.Ready1"),
	CMD_SHP_ADD_READY2("Command.Shop.Add.Ready2"),

	CMD_SHP_BUY_NO_ITEM_IN_HAND("Command.Shop.Buy.NoItemInHand"),
	CMD_SHP_BUY_MINIMUM_ONE("Command.Shop.Buy.MinimumOfOne"),
	CMD_SHP_BUY_USAGE("Command.Shop.Buy.Usage"),
	CMD_SHP_BUY_PLAYERS_ONLY("Command.Shop.Buy.PlayersOnly"),
	CMD_SHP_BUY_SHOP_NOT_SELLING("Command.Shop.Buy.ShopNotSellingItem"),
	CMD_SHP_BUY_SHOP_SOLD_OUT("Command.Shop.Buy.ShopSoldOutItem"),
	CMD_SHP_BUY_SHOP_HAS_QTY("Command.Shop.Buy.ShopHasQuantity"),
	CMD_SHP_BUY_ORDER_REDUCED("Command.Shop.Buy.OrderReduced"),
	CMD_SHP_BUY_PLAYER_INSUFFICIENT_ROOM("Command.Shop.Buy.PlayerInsufficientRoom"),
	CMD_SHP_BUY_PLAYER_AFFORD_NONE("Command.Shop.Buy.PlayerCanAffordNone"),
	CMD_SHP_BUY_PLAYER_AFFORD_QTY("Command.Shop.Buy.PlayerCanAffordQuantity"),
	CMD_SHP_BUY_REMOVED_QTY("Command.Shop.Buy.RemovedQuantity"),
	CMD_SHP_BUY_PURCHASED_QTY("Command.Shop.Buy.PurchasedQuantity"),

	CMD_SHP_CREATE_MAX_NUM_SHOPS("Command.Shop.Create.MaximumNumberShops"),
	CMD_SHP_CREATE_SELECTION_PROB_SIZE("Command.Shop.Create.SelectionSizeProblem"),
	CMD_SHP_CREATE_SELECTION_PROB_ONLY_ONE_POINT("Command.Shop.Create.SelectionOnePointProblem"),
	CMD_SHP_CREATE_SHOP_EXISTS("Command.Shop.Create.ShopExists"),
	CMD_SHP_CREATE_WORLD_HAS_GLOBAL("Command.Shop.Create.WorldShopExists"),
	CMD_SHP_CREATE_INSUFFICIENT_FUNDS("Command.Shop.Create.InsufficientFunds"),
	CMD_SHP_CREATE_LOG("Command.Shop.Create.Log"),
	CMD_SHP_CREATE_SUCCESS("Command.Shop.Create.Success"),
	CMD_SHP_CREATE_FAIL("Command.Shop.Create.Fail"),

	CMD_SHP_SET_BUNDLE_FAIL("Command.Shop.Set.Bundles.UnderOne"),

	CMD_SHP_NOT_ON_WORLD("Command.Shop.Addloc.NotOnWorld");
	
	String msg = null;

	Messages(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}
