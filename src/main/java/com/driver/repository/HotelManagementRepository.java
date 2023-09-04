package com.driver.repository;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class HotelManagementRepository {


    public static class Pair{
        String name;
        int size;

        public Pair(String name, int size) {
            this.name = name;
            this.size = size;
        }
    }

    HashMap<String, Hotel> hotelDb = new HashMap<>();
    HashMap<Integer, User> userDb = new HashMap<>();
    HashMap<String, Booking> bookingDb = new HashMap<>();
    public String addHotel(Hotel hotel) {
        if(hotel != null && hotel.getHotelName() != null && !hotelDb.containsKey(hotel.getHotelName())){
            hotelDb.put(hotel.getHotelName(), hotel);
            return "SUCCESS";
        }
        return "FAILURE";
    }

    public int addUser(User user) {
        int cardNo = user.getaadharCardNo();
        userDb.put(cardNo, user);
        return cardNo;
    }

    public String getHotelWithMostFacilities() {
        List<Pair> facilities = new ArrayList<>();
        int count = 0;
        for(String name : hotelDb.keySet()){
            int size = hotelDb.get(name).getFacilities().size();
            count = Math.max(count, size);
            facilities.add(new Pair(name, size));
        }
        Collections.sort(facilities, (a,b) -> a.name.compareTo(b.name));

        if(count != 0) {
            for (int i = 0; i < hotelDb.size(); i++) {
                Pair curr = facilities.get(i);
                int num = curr.size;
                if (num == count) return curr.name;
            }
        }
        return "";
    }

    public int addARoom(Booking booking) {
        String hotelName = booking.getHotelName();
        int roomAvail = hotelDb.get(hotelName).getAvailableRooms();
        int noOfRooms = booking.getNoOfRooms();
        if(roomAvail >= noOfRooms){
            String uiid = String.valueOf(UUID.randomUUID());
            int charges = hotelDb.get(hotelName).getPricePerNight();

            int amount = noOfRooms*charges;
            booking.setAmountToBePaid(amount);
            booking.setBookingId(uiid);
            bookingDb.put(uiid, booking);

            //update the roomAvailable of hotelDb
            hotelDb.get(hotelName).setAvailableRooms(roomAvail-noOfRooms);
            return amount;
        }
        return -1;
    }

    public int getBookings(Integer aadharCard) {
        int count = 0;
        for(String id : bookingDb.keySet()){
            int aadhar = bookingDb.get(id).getBookingAadharCard();
            if(aadharCard == aadhar){
                count++;
            }
        }
        return count;
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        List<Facility> facilityList = hotelDb.get(hotelName).getFacilities();

        HashSet<Facility> set = new HashSet<>();
        for(int i = 0; i < facilityList.size(); i++){
            if(!set.contains(facilityList.get(i))){
                set.add(facilityList.get(i));
            }
        }

        for(Facility facility : newFacilities){
            if(!set.contains(facility)){
                set.add(facility);
                facilityList.add(facility);
            }
        }

        hotelDb.get(hotelName).setFacilities(facilityList);
        return hotelDb.get(hotelName);
    }
}