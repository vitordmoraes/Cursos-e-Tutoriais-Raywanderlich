/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.raywenderlich.android.trippey.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.ContactsContract
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.raywenderlich.android.trippey.database.DatabaseConstants.COLUMN_LOCATIONS
import com.raywenderlich.android.trippey.database.DatabaseConstants.DATABASE_NAME
import com.raywenderlich.android.trippey.database.DatabaseConstants.DATABASE_VERSION
import com.raywenderlich.android.trippey.database.DatabaseConstants.QUERY_BY_ID
import com.raywenderlich.android.trippey.database.DatabaseConstants.SQL_CREATE_ENTRIES
import com.raywenderlich.android.trippey.database.DatabaseConstants.SQL_DELETE_ENTRIES
import com.raywenderlich.android.trippey.database.DatabaseConstants.SQL_UPDATE_DATABASE_ADD_LOCATIONS
import com.raywenderlich.android.trippey.database.DatabaseConstants.TRIP_TABLE_NAME
import com.raywenderlich.android.trippey.model.Trip
import com.raywenderlich.android.trippey.model.TripLocation
import java.util.*

class TrippeyDatabase(
  context: Context,
  private val gson: Gson
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

  override fun onCreate(database: SQLiteDatabase?) {
    database?.execSQL(SQL_CREATE_ENTRIES)
  }

  override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    if (oldVersion == 1 && newVersion ==  2){
      database?.execSQL(SQL_UPDATE_DATABASE_ADD_LOCATIONS)
    } else {
      database?.execSQL(SQL_DELETE_ENTRIES)
      onCreate(database)
    }
  }

  fun saveTrip(trip: Trip) {
    val database = writableDatabase ?: return

    val newValues = ContentValues().apply {
      put(DatabaseConstants.COLUMN_ID, trip.id)
      put(DatabaseConstants.COLUMN_TITLE, trip.title)
      put(DatabaseConstants.COLUMN_COUNTRY, trip.country)
      put(DatabaseConstants.COLUMN_DETAILS, trip.details)
      put(DatabaseConstants.COLUMN_IMAGE_URL, trip.imageUrl)
      put(DatabaseConstants.COLUMN_LOCATIONS, gson.toJson(trip.locations))
    }

    database.insert(DatabaseConstants.TRIP_TABLE_NAME, null, newValues)
  }

  fun updateTrip(trip: Trip) {
  val database = writableDatabase ?: return
  val updatedLocations = gson.toJson(trip.locations)
  val newValeus = ContentValues().apply {
    put(COLUMN_LOCATIONS, updatedLocations)
  }

  val selection = QUERY_BY_ID
  val selectionArguments = arrayOf(trip.id)

  database.update(TRIP_TABLE_NAME, newValeus, selection, selectionArguments)
  }

  fun deleteTrip(tripId: String) {
    val database = writableDatabase ?: return

    val selection = QUERY_BY_ID
    val selectionArguments = arrayOf(tripId)

    database.delete(TRIP_TABLE_NAME, selection, selectionArguments)
  }

  fun getTrips(): List<Trip> {
    val items = mutableListOf<Trip>()
    val database = readableDatabase ?: return items

    val cursor = database.query(
      DatabaseConstants.TRIP_TABLE_NAME,
      null,
      null,
      null,
      null,
      null,
      null
    )

    while (cursor.moveToNext()) {
      items.add(
        Trip(
          cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.COLUMN_ID)),
          cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.COLUMN_TITLE)),
          cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.COLUMN_COUNTRY)),
          cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.COLUMN_DETAILS)),
          cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.COLUMN_IMAGE_URL)),
          parseTripLocationsFromJson(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATIONS)))
        )
      )
    }

    cursor.close()
    return items
  }

  private fun parseTripLocationsFromJson(json: String?): List<TripLocation> {
    if (json == null) return emptyList()

    val typeToken = object : TypeToken<List<TripLocation>>() {}.type

    return try {
      gson.fromJson(json, typeToken)
    } catch (error: Throwable) {
      error.printStackTrace()
      emptyList()
    }
  }

}