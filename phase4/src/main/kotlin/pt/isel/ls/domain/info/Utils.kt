package pt.isel.ls.domain.info

/**
 * The information about the game.
 * Used to retrieve a session.
 *
 * [UInt] is the identifier of the game.
 * [String] is the name of the game.
 */
typealias GameInfoParam = Pair<UInt?, String?>

/**
 * The information about the player.
 * Used to retrieve a session.
 *
 * [UInt] is the identifier of the player.
 * [String] is the username of the player.
 */
typealias PlayerInfoParam = Pair<UInt?, String?>
