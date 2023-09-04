package com.driver.repository;


import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@Repository
public class HotelManagementRepository {

    HashMap<String, Hotel> hotelDb;
    HashMap<Integer, User> userDb;

    HashMap<String, List<Facility>> hotelFacilitiesDB;

    HashMap<String, Booking> bookingDB;

    HashMap<String,List<String>> hotelBookingDB;

    HashMap<Integer,List<String>> userBookingDB;

    public HotelManagementRepository()
    {
        hotelDb = new HashMap<>();
        userDb = new HashMap<>();
        hotelFacilitiesDB = new HashMap<>();
        bookingDB = new HashMap<>();
        hotelBookingDB = new HashMap<>();
        userBookingDB = new HashMap<>();


    }

    public String addHotel(Hotel hotel) {

        if(hotel == null || hotel.getHotelName() == null)
        {
            return "FAILURE";
        }

        String hotelName = hotel.getHotelName();
        if(hotelDb.containsKey(hotelName))
        {
            return "FAILURE";
        }


        hotelDb.put(hotelName,hotel);

        List<Facility> facilities = hotel.getFacilities();

        hotelFacilitiesDB.put(hotelName, facilities);

        return "SUCCESS";

    }

    public Integer addUser(User user) {

        int aadharCardNo = user.getaadharCardNo();
        userDb.put(aadharCardNo, user);
        return aadharCardNo;
    }

    public String getHotelWithMostFacilities() {

        String hotelName = "";
        int max = 0;

        for(String hotel : hotelFacilitiesDB.keySet())
        {
            if(max < hotelFacilitiesDB.get(hotel).size())
            {
                max = hotelFacilitiesDB.get(hotel).size();
                hotelName = hotel;
            }
            else if(max == hotelFacilitiesDB.get(hotel).size() && max != 0)
            {
                if (hotelName.compareTo(hotel) > 0)
                {
                    hotelName = hotel;
                }
            }
        }

        return hotelName;
    }

    public int bookARoom(Booking booking) {

//        bookingDB.put(booking.getBookingId(),booking);
//
//        Hotel hotel = hotelDb.get(booking.getHotelName());
//
//        if(hotel.getAvailableRooms() < booking.getNoOfRooms())
//        {
//            return -1;
//        }
//        hotel.setAvailableRooms(hotel.getAvailableRooms() - booking.getNoOfRooms());
//        int totalAmount = hotel.getPricePerNight() * booking.getNoOfRooms();
//        booking.setAmountToBePaid(totalAmount);
//
//        List<String> bookingList = new ArrayList<>();
//
//        if(hotelBookingDB.containsKey(booking.getHotelName()))
//        {
//            bookingList = hotelBookingDB.get(booking.getHotelName());
//        }
//
//        bookingList.add(booking.getBookingId());
//        hotelBookingDB.put(booking.getHotelName(), bookingList);
//
//
//        List<String> userBookingList = new ArrayList<>();
//
//        if(userBookingDB.containsKey(booking.getBookingAadharCard()))
//        {
//            userBookingList = userBookingDB.get(booking.getBookingAadharCard());
//        }
//        userBookingList.add(booking.getBookingId());
//
//        userBookingDB.put(booking.getBookingAadharCard(), userBookingList);
//
//        return totalAmount;

        String hotelName = booking.getHotelName();
        int roomAvail = hotelDb.get(hotelName).getAvailableRooms();
        int noOfRooms = booking.getNoOfRooms();
        if(roomAvail >= noOfRooms){
            String uiid = String.valueOf(UUID.randomUUID());
            int charges = hotelDb.get(hotelName).getPricePerNight();

            int amount = noOfRooms*charges;
            booking.setAmountToBePaid(amount);
            booking.setBookingId(uiid);
            bookingDB.put(uiid, booking);

            //update the roomAvailable of hotelDb
            hotelDb.get(hotelName).setAvailableRooms(roomAvail-noOfRooms);
            return amount;
        }
        return -1;

    }

    public int getBookings(Integer aadharCard) {

        int count = 0;
        for(String id : bookingDB.keySet()){
            int aadhar = bookingDB.get(id).getBookingAadharCard();
            if(aadharCard == aadhar){
                count++;
            }
        }
        return count;
    }


    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {

        List<Facility> currFacilitiesList = hotelFacilitiesDB.get(hotelName);

        for (Facility facility : newFacilities)
        {
            if(!currFacilitiesList.contains(facility))
            {
                currFacilitiesList.add(facility);
            }
        }

        hotelDb.get(hotelName).setFacilities(currFacilitiesList);
        hotelFacilitiesDB.put(hotelName, currFacilitiesList);

        return hotelDb.get(hotelName);
    }
}