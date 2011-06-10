LocalShops - A cuboid shop plugin for Bukkit
==================

A localized shop system that lets users create their own shops and then control the inventory inside of them.


Changelog
-------------------


	* Features
		- Shop Signs
		- Global Shops
		- Dynamically Priced Shops
		- More Block support (1.6 blocks + 'Hacky' ones)

Version 3.1.0
    * Out of Beta!
    * Features
       - Permissions updated, is not mandatory 
          - Will only restrict admin commands to ops without Permissions
       - Sorting of /shop browse and /shop list
       - New command: /shop find
          - Used to find nearby shops that sell an item
       - Transaction notification for shop owners
    * Bug Fixes
       - Economy sanity checks
       - /shop select Message clarify usage (bare hands)
       - Sanity checks on corrupt shop files
       - /shop sell all now sells all items instead of a single stack
       - Refactored Commands (now each in their own class)
       - Remove players from memory on PLAYER_KICK and PLAYER_QUIT events
       - Notification for insufficient funds
       - Owner & creator information is now shown in /shop info for all situations
       - Match criteria updated
          - Wool, Cactus Green, Cactus, Glowstone Dust

Version 3.0.3 Beta
    * Fixed money exploit
    * Fix typo on selling or buying 0 items
    * Redstone Dust match criteria updated
    * Soul Sand match criteria updated

Version 3.0.2 Beta
    * Fixed IllegalFormatConversionException on /sell

Version 3.0.1 Beta
    * Item durability checks & messages
    * Fix regex groupings
    * Converted shops are now moved to plugins/LocalShops/shops-converted/
    * Broken shops are now moved to plugins/LocalShops/shops-broken/
    * Report LocalShops UUID during onEnable()

Version 3.0.0 Beta
    * Update default settings

Version 3.0.0 Alpha 6
    * "/shop set name" is now in help
    * Add minimum balance for shops (see /shop set minbalance) - Sleaker
    * Default values for shop settings
    * Decimal support
    * Code optimization

Version 3.0.0 Alpha 5
    * Essentials Economy now uses included formatting.
    * Items update: Ice corrected
    * /shop move [ShopID] is now correctly shown in /shop help
    * Initial implementation of maximum stack sizes (not in use yet)
    * Shop file loading sanity checks
         - Invalid shops will be moved to the "broken-shops" folder.

Version 3.0.0 Alpha 4
    * Essentials Economy fix
    * Add instructions for buying/selling after /shop add item

Version 3.0.0 Alpha 3
    * Added debug setting (debug=true in config file enables verbose console messages)
    * Added additional messages to /shop select
    * Fixed /shop list all (now shows all shops)

Version 3.0.0 Alpha 2
    * Unsupported items no longer cause a NullPointerException, the shop is skipped.
    * Permissions for free items have been moved:
         - localshops.free.create
         - localshops.free.move
    * Output now correctly uses economy values (ie: showing the correct names)
    * Added missing items

Version 3.0.0 Alpha 1
    * /shop list on Console shows the owner of the Shops
    * Implementation of maximum shops per player (via config file: 'shops-per-player'), default is unlimited
    * Fix Exceptions when iConomy 5 was not present during load.
    * Players now receive a shop's inventory during /shop destroy
    * Various smaller internal fixes.

Version 3.0.0 Alpha 0
    * Initial version of LocalShops 3 (alpha)
    * /shop list [all]: Shows a list of shops
    * /shop browse [buy|sell]: Replaces previous v2.2 of /shop list
    * /shop reload: Removed (it had issues)
    * /shop debug [id]: Provides in-depth logging to console about the shop, useful for troubleshooting or more specifically when looking for bugs.
    * LocalShops now uses Dark Aqua instead of Aqua for our messages.
    * /shop info [id]: Provides information in-game about a shop.
    * /shop move [id]: Now uses id's instead of names
    * Multiple shop's can now use the same name.
    * /buy & /sell are short for /shop buy ... and /shop sell ...
    * During /shop set owner, reset sell prices (prevents abuse)
    * "/shop buy [item] 0" Now complains about buying 0 quantity of an item
    * Support for multiple Economies (iConomy 4, iConomy 5, BOSEconomy, Essentials Economy)
    * /shop select now only works with empty hands.
    * Implemented Reporting Thread (anonymous usage tracking)
    * Removed items.txt, now built in directly.
    * Item Searches are no longer case or space sensitive.
    * Default Max Damage for an Item is 35% (aka, items with less than 65% remaining cannot be added or sold to a store).
    * Shop coordinates being slightly off are now fixed.
    * LocalShops now uses /lshop as the base command with aliases allowing for /shop
