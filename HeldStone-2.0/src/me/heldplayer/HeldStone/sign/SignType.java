package me.heldplayer.HeldStone.sign;

public enum SignType {
	UNKNOWN("Unknown", null, null),
	ANNOUNCE("Announce", "heldstone.ic.chat.announce", me.heldplayer.HeldStone.sign.chat.AnnounceSign.class),
	GLANNOUNCE("GlAnnounce", "heldstone.ic.chat.glannounce", me.heldplayer.HeldStone.sign.chat.GlAnnounceSign.class),
	DISP("Disp", "heldstone.ic.chat.disp", me.heldplayer.HeldStone.sign.chat.DispSign.class);

	public final String displayName;
	public final String permission;
	public final Class<? extends HeldSign> signClass;

	private SignType(String displayName, String permission, Class<? extends HeldSign> clazz) {
		this.displayName = displayName;
		this.permission = permission;
		this.signClass = clazz;
	}
}
