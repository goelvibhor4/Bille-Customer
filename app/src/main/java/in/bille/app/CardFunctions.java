package in.bille.app;

/**
 * Created by Vibhor Goel on 2/12/2016.
 */
public class CardFunctions {
    int _id;
    String _name;
    String _phone_number;
    String _card_number;
    String _card_month;
    String _card_year;


    public CardFunctions(){   }
    public CardFunctions(int id, String name, String _phone_number,String _card_number, String _card_month, String _card_year){
        this._id = id;
        this._name = name;
        this._phone_number = _phone_number;
        this._card_number = _card_number;
        this._card_month = _card_month;
        this._card_year = _card_year;



    }

    public CardFunctions(String name, String _phone_number,String _card_number, String _card_month, String _card_year){
        this._name = name;
        this._phone_number = _phone_number;
        this._card_number = _card_number;
        this._card_month = _card_month;
        this._card_year = _card_year;



    }
    public int getID(){
        return this._id;
    }

    public void setID(int id){
        this._id = id;
    }

    public String getName(){
        return this._name;
    }

    public void setName(String name){
        this._name = name;
    }

    public String getPhoneNumber(){
        return this._phone_number;
    }

    public void setPhoneNumber(String phone_number){
        this._phone_number = phone_number;
    }



    public String getCardNumber(){
        return this._card_number;
    }

    public void setCardNumber(String card_number){
        this._card_number = card_number;
    }


    public String getCardMonth(){
        return this._card_month;
    }

    public void setCardMonth(String card_month){
        this._card_month = card_month;
    }

    public String getCardYear(){
        return this._card_year;
    }

    public void setCardYear(String card_year){
        this._card_year = card_year;
    }



}