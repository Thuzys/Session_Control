package pt.isel.ls.domain

/**
 * The common characteristics to all Application's Domain.
 *
 * @param uuid the identifier of each Item.
 */
sealed class Domain(open val uuid: UInt? = null)
