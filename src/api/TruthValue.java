package api;

import util.DeepCloneable;


public enum TruthValue implements DeepCloneable {
	True, False, Unknown;
	public boolean isTrue() {
		return ordinal() == 0;
	}
	public boolean isFalse() {
		return ordinal() == 1;
	}
	public boolean isUnknown() {
		return ordinal() >= 2;
	}
	
	public TruthValue deepClone() {
		return this;
	}
	
}
