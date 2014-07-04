package ru.arcadoss.collagemaker.json;

import java.util.Comparator;

/**
 * Created by arcturus at 03.07.14
 */
public class Entry {
	public final int likesCount;
	public final EntryPhotos photos;
	public static final Comparator<Entry> LIKES_COMPARATOR = new Comparator<Entry>() {
		@Override
		public int compare(Entry lhs, Entry rhs) {
			return (int) Math.signum(rhs.likesCount - lhs.likesCount);
		}
	};

	public Entry(int likesCount, EntryPhotos photos) {
		this.likesCount = likesCount;
		this.photos = photos;
	}
}
