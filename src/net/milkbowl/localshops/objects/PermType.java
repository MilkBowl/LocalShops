package net.milkbowl.localshops.objects;

public enum PermType {
	ADMIN_LOCAL("localshops.admin.local"),
	ADD("localshops.manager.add"),
	BUY("localshops.local.buy"),
	CREATE("localshops.manager.create"),
	CREATE_FREE("localshops.free.create"),
	DESTROY("localshops.manager.destroy"),
	HELP(""),
	BROWSE("localshops.local.browse"),
	MOVE("localshops.manager.move"),
	MOVE_FREE("localshops.free.move"),
	REMOVE("localshops.manager.remove"),
	SEARCH(null),
	SELECT("localshops.manager.select"),
	SELL("localshops.local.sell"),
	SET_OWNER("localshops.manager.set.owner"),
	SET("localshops.manager.set"),
	GLOBAL_BUY("localshops.global.buy"),
	GLOBAL_SELL("localshops.global.sell"),
	ADMIN_GLOBAL("localshops.admin.global"),
	ADMIN_SERVER("localshops.admin.server"),
	GLOBAL_BROWSE("localshops.global.browse"),
	MULTI_LOCATION("localshops.local.multilocation");


	String permissions = null;

	PermType(String permissions) {
		this.permissions = permissions;
	}

	public String get() {
		return permissions;
	}

}
