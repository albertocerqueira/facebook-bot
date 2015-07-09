/**
 * Autogenerated by Thrift
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 */
package facebook.bot.cassandra;

/*
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

import java.util.BitSet;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.thrift.TBase;
import org.apache.thrift.TBaseHelper;
import org.apache.thrift.TException;
import org.apache.thrift.TFieldIdEnum;
import org.apache.thrift.TFieldRequirementType;
import org.apache.thrift.meta_data.FieldMetaData;
import org.apache.thrift.meta_data.FieldValueMetaData;
import org.apache.thrift.protocol.TField;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.protocol.TProtocolUtil;
import org.apache.thrift.protocol.TStruct;
import org.apache.thrift.protocol.TType;

/**
 * Encapsulate types of conflict resolution.
 * 
 * @param timestamp
 *            . User-supplied timestamp. When two columns with this type of
 *            clock conflict, the one with the highest timestamp is the one
 *            whose value the system will converge to. No other assumptions are
 *            made about what the timestamp represents, but using
 *            microseconds-since-epoch is customary.
 */
public class Clock implements TBase<Clock, Clock._Fields>, java.io.Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	private static final TStruct STRUCT_DESC = new TStruct("Clock");

	private static final TField TIMESTAMP_FIELD_DESC = new TField("timestamp", TType.I64, (short) 1);

	public long timestamp;

	/**
	 * The set of fields this struct contains, along with convenience methods
	 * for finding and manipulating them.
	 */
	public enum _Fields implements TFieldIdEnum {
		TIMESTAMP((short) 1, "timestamp");

		private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

		static {
			for (_Fields field : EnumSet.allOf(_Fields.class)) {
				byName.put(field.getFieldName(), field);
			}
		}

		/**
		 * Find the _Fields constant that matches fieldId, or null if its not
		 * found.
		 */
		public static _Fields findByThriftId(int fieldId) {
			switch (fieldId) {
			case 1: // TIMESTAMP
				return TIMESTAMP;
			default:
				return null;
			}
		}

		/**
		 * Find the _Fields constant that matches fieldId, throwing an exception
		 * if it is not found.
		 */
		public static _Fields findByThriftIdOrThrow(int fieldId) {
			_Fields fields = findByThriftId(fieldId);
			if (fields == null)
				throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
			return fields;
		}

		/**
		 * Find the _Fields constant that matches name, or null if its not
		 * found.
		 */
		public static _Fields findByName(String name) {
			return byName.get(name);
		}

		private final short _thriftId;
		private final String _fieldName;

		_Fields(short thriftId, String fieldName) {
			_thriftId = thriftId;
			_fieldName = fieldName;
		}

		public short getThriftFieldId() {
			return _thriftId;
		}

		public String getFieldName() {
			return _fieldName;
		}
	}

	// isset id assignments
	private static final int __TIMESTAMP_ISSET_ID = 0;
	private BitSet __isset_bit_vector = new BitSet(1);

	public static final Map<_Fields, FieldMetaData> metaDataMap;
	static {
		Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
		tmpMap.put(_Fields.TIMESTAMP, new FieldMetaData("timestamp", TFieldRequirementType.REQUIRED, new FieldValueMetaData(TType.I64)));
		metaDataMap = Collections.unmodifiableMap(tmpMap);
		FieldMetaData.addStructMetaDataMap(Clock.class, metaDataMap);
	}

	public Clock() {
	}

	public Clock(long timestamp) {
		this();
		this.timestamp = timestamp;
		setTimestampIsSet(true);
	}

	/**
	 * Performs a deep copy on <i>other</i>.
	 */
	public Clock(Clock other) {
		__isset_bit_vector.clear();
		__isset_bit_vector.or(other.__isset_bit_vector);
		this.timestamp = other.timestamp;
	}

	public Clock deepCopy() {
		return new Clock(this);
	}

	@Deprecated
	public Clock clone() {
		return new Clock(this);
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	public Clock setTimestamp(long timestamp) {
		this.timestamp = timestamp;
		setTimestampIsSet(true);
		return this;
	}

	public void unsetTimestamp() {
		__isset_bit_vector.clear(__TIMESTAMP_ISSET_ID);
	}

	/**
	 * Returns true if field timestamp is set (has been asigned a value) and
	 * false otherwise
	 */
	public boolean isSetTimestamp() {
		return __isset_bit_vector.get(__TIMESTAMP_ISSET_ID);
	}

	public void setTimestampIsSet(boolean value) {
		__isset_bit_vector.set(__TIMESTAMP_ISSET_ID, value);
	}

	public void setFieldValue(_Fields field, Object value) {
		switch (field) {
		case TIMESTAMP:
			if (value == null) {
				unsetTimestamp();
			} else {
				setTimestamp((Long) value);
			}
			break;

		}
	}

	public void setFieldValue(int fieldID, Object value) {
		setFieldValue(_Fields.findByThriftIdOrThrow(fieldID), value);
	}

	public Object getFieldValue(_Fields field) {
		switch (field) {
		case TIMESTAMP:
			return new Long(getTimestamp());

		}
		throw new IllegalStateException();
	}

	public Object getFieldValue(int fieldId) {
		return getFieldValue(_Fields.findByThriftIdOrThrow(fieldId));
	}

	/**
	 * Returns true if field corresponding to fieldID is set (has been asigned a
	 * value) and false otherwise
	 */
	public boolean isSet(_Fields field) {
		switch (field) {
		case TIMESTAMP:
			return isSetTimestamp();
		}
		throw new IllegalStateException();
	}

	public boolean isSet(int fieldID) {
		return isSet(_Fields.findByThriftIdOrThrow(fieldID));
	}

	@Override
	public boolean equals(Object that) {
		if (that == null)
			return false;
		if (that instanceof Clock)
			return this.equals((Clock) that);
		return false;
	}

	public boolean equals(Clock that) {
		if (that == null)
			return false;

		boolean this_present_timestamp = true;
		boolean that_present_timestamp = true;
		if (this_present_timestamp || that_present_timestamp) {
			if (!(this_present_timestamp && that_present_timestamp))
				return false;
			if (this.timestamp != that.timestamp)
				return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	public int compareTo(Clock other) {
		if (!getClass().equals(other.getClass())) {
			return getClass().getName().compareTo(other.getClass().getName());
		}

		int lastComparison = 0;
		Clock typedOther = (Clock) other;

		lastComparison = Boolean.valueOf(isSetTimestamp()).compareTo(typedOther.isSetTimestamp());
		if (lastComparison != 0) {
			return lastComparison;
		}
		if (isSetTimestamp()) {
			lastComparison = TBaseHelper.compareTo(this.timestamp, typedOther.timestamp);
			if (lastComparison != 0) {
				return lastComparison;
			}
		}
		return 0;
	}

	public void read(TProtocol iprot) throws TException {
		TField field;
		iprot.readStructBegin();
		while (true) {
			field = iprot.readFieldBegin();
			if (field.type == TType.STOP) {
				break;
			}
			switch (field.id) {
			case 1: // TIMESTAMP
				if (field.type == TType.I64) {
					this.timestamp = iprot.readI64();
					setTimestampIsSet(true);
				} else {
					TProtocolUtil.skip(iprot, field.type);
				}
				break;
			default:
				TProtocolUtil.skip(iprot, field.type);
			}
			iprot.readFieldEnd();
		}
		iprot.readStructEnd();

		// check for required fields of primitive type, which can't be checked
		// in the validate method
		if (!isSetTimestamp()) {
			throw new TProtocolException("Required field 'timestamp' was not found in serialized data! Struct: " + toString());
		}
		validate();
	}

	public void write(TProtocol oprot) throws TException {
		validate();

		oprot.writeStructBegin(STRUCT_DESC);
		oprot.writeFieldBegin(TIMESTAMP_FIELD_DESC);
		oprot.writeI64(this.timestamp);
		oprot.writeFieldEnd();
		oprot.writeFieldStop();
		oprot.writeStructEnd();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Clock(");
		boolean first = true;

		sb.append("timestamp:");
		sb.append(this.timestamp);
		first = false;
		sb.append(")");
		return sb.toString();
	}

	public void validate() throws TException {
		// check for required fields
		// alas, we cannot check 'timestamp' because it's a primitive and you
		// chose the non-beans generator.
	}

	public _Fields fieldForId(int fieldId) {
		return null;
	}

	public void clear() {
		
	}
}