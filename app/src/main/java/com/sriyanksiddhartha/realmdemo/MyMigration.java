package com.sriyanksiddhartha.realmdemo;


import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class MyMigration implements RealmMigration {

	@Override
	public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

		RealmSchema schema = realm.getSchema();

		if (oldVersion == 0) {
			// Upgrade your schema
			RealmObjectSchema userSchema = schema.get("User");
			userSchema.addField("hobby", String.class);
			oldVersion++;
		}

		if (oldVersion == 1) {

			RealmObjectSchema companySchema = schema.create("Company");
			companySchema.addField("name", String.class);

			RealmObjectSchema userSchema = schema.get("User");
			userSchema.addRealmObjectField("company", companySchema);

			oldVersion++;
		}
	}
}
