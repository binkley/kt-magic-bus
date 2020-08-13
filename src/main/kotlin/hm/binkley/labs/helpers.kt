package hm.binkley.labs

/**
 * Subscribes [mailbox] to [this] bus without caller providing a type object.
 *
 * @see MagicBus.subscribe
 * @see subscribeTo
 */
inline fun <reified T : Any> MagicBus.subscribe(
    noinline mailbox: Mailbox<in T>,
) = subscribe(T::class.java, mailbox)

/** An alternative, fluent syntax to [subscribe]. */
inline fun <reified T : Any, M : Mailbox<T>> M.subscribeTo(
    bus: MagicBus,
): M {
    bus += this
    return this
}

/** A syntactic alternative to [subscribe]. */
inline operator fun <reified T : Any, M : Mailbox<T>> MagicBus.plusAssign(
    mailbox: M
) = subscribe(mailbox)

/**
 * Unsubscribes without caller providing a type object.
 *
 * @see MagicBus.unsubscribe
 * @see unsubscribeFrom
 */
inline fun <reified T : Any> MagicBus.unsubscribe(
    noinline mailbox: Mailbox<in T>,
) = unsubscribe(T::class.java, mailbox)

/** An alternative, fluent syntax to [unsubscribe]. */
inline fun <reified T : Any, M : Mailbox<T>> M.unsubscribeFrom(
    bus: MagicBus,
): M {
    bus -= this
    return this
}

/** A syntactic alternative to [unsubscribe]. */
inline operator fun <reified T : Any, M : Mailbox<T>> MagicBus.minusAssign(
    mailbox: M
) = unsubscribe(mailbox)

/** Creates a mailbox wrapping [receive], and a [toString] of [name]. */
fun <T> namedMailbox(name: String, receive: Mailbox<T>) =
    object : Mailbox<T> {
        override fun invoke(message: T) = receive(message)
        override fun toString() = name
    }

/**
 * Creates a mailbox which throws away messages.  Use case: To ignore posts
 * for messages of type [T], use a discard mailbox.
 */
inline fun <reified T : Any> discard(): Mailbox<T> =
    namedMailbox("DISCARD-MAILBOX<${T::class.java.simpleName}>") { }
