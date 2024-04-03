package com.example.alogrithimsimplifier;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.SpannedString;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    Button simplify;
    Button undo;
    Button plus;
    Button minus;
    Button exponent;
    Button x;
    Button y;
    String presentEquation;
    String backgroundEquation;
    TextView past;
    TextView present;
    boolean nextButtonIsExponent;
    int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        simplify = findViewById(R.id.button_simplify);
        undo = findViewById(R.id.button_undo);
        plus = findViewById(R.id.button_plus);
        minus = findViewById(R.id.button_minus);
        exponent = findViewById(R.id.button_expo);
        x = findViewById(R.id.button_X);
        y = findViewById(R.id.button_Y);
        past = findViewById(R.id.past);
        present = findViewById(R.id.present);
        nextButtonIsExponent = false;
        presentEquation = "";
        backgroundEquation ="";
        count = 0;

    }
    public void Update()
    {
        if(count >0)
        {
            past.setText(present.getText());
            count--;
        }
        present.setText(Html.fromHtml(presentEquation));
    }

    public void OnClickUndo(View view)
    {
        try
        {
            if (!presentEquation.equals("0") || presentEquation.length() > 0) {
                presentEquation = presentEquation.substring(0, presentEquation.length() - 1);
                backgroundEquation = backgroundEquation.substring(0, backgroundEquation.length() - 1);
                Update();
            }
        }
        catch(Exception error)
        {
            present.setText("Nothing to Undo");
            presentEquation = "";
            backgroundEquation = "";
        }
    }

    public void OnClickPlus(View view)
    {
        presentEquation += "+";
        backgroundEquation += " +";
        Update();
    }
    public void OnClickMinus(View view)
    {
        presentEquation += "-";
        backgroundEquation += " -";
        Update();
    }
    public void OnClickExpo(View view)
    {
        nextButtonIsExponent = true;
    }
    public void OnClickNumericButton(View view)
    {
        if(nextButtonIsExponent)
        {
            String expoVal = ((Button) view).getText().toString();
            String lmao = "<html><sup>"+expoVal+"</sup></html>";
            backgroundEquation += "^" + expoVal;
            UpdateSpecial(lmao);
            nextButtonIsExponent = false;
        }
        else
        {
            presentEquation += ((Button) view).getText().toString();
            backgroundEquation += ((Button) view).getText().toString();
            Update();
        }
    }
    public void UpdateSpecial(String r)
    {
       presentEquation += r;
       Update();
    }
    public void OnClickX(View view)
    {
        presentEquation += "x";
        backgroundEquation += "x";
        Update();
    }
    public void OnClickY(View view) {
        presentEquation += "y";
        backgroundEquation += "y";
        Update();
    }

    public void OnClickSimplify(View view)
    {
        count++;
        past.setText(Html.fromHtml(presentEquation));
        try {
            present.setText(Html.fromHtml( simplify(backgroundEquation) ) );
        } catch(Exception error)
        { present.setText("Number is Too Big");}
        presentEquation = "";
        backgroundEquation = "";

    }
    public String simplify(String r)
    {
        ArrayList<String> ar = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(r, " ", false);
        while(tokenizer.hasMoreTokens())
        {
            ar.add(tokenizer.nextToken());
        }

        ArrayList<String> expox = new ArrayList<String>();
        ArrayList<String> varx = new ArrayList<String>();
        ArrayList<String> expoy = new ArrayList<String>();
        ArrayList<String> vary = new ArrayList<String>();
        ArrayList<String> not = new ArrayList<String>();

        for(int i = 0; i< ar.size(); i++)
        {
            if(ar.get(i).contains("x^"))
            {
                expox.add(ar.get(i));
            }
            else if(ar.get(i).contains("y^"))
            {
                expoy.add(ar.get(i));
            }
            else if(ar.get(i).contains("x"))
            {
                varx.add(ar.get(i));
            }
            else if(ar.get(i).equals("-") )
            {
                return "Error with Equation";
            }
            else if(ar.get(i).equals("+") )
            {
                return "Error with Equation";
            }
            else if(ar.get(i).contains("y"))
            {
                vary.add(ar.get(i));
            }
            else
            {
                not.add(ar.get(i));
            }
        }

        ArrayList<String> sortedExpox = new ArrayList<String>();
        for(int i = expox.size()-1; i>=0; i--)
        {
            String l = expox.get(i);
            for(int j = i-1; j>=0; j--)
            {
                if( expox.get(i).substring( expox.get(i).indexOf("^") ).equals( expox.get(j).substring( expox.get(j).indexOf("^") ) ) )
                {
                    l += " " + expox.get(j);
                }
            }
            sortedExpox.add(l);
        }

        for( int i= 0; i<sortedExpox.size(); i++)
        {
            String l = sortedExpox.get(i);
            int v = i+1;

            while( v < sortedExpox.size() )
            {
                if( l.contains( sortedExpox.get(v) ) )
                {
                    sortedExpox.remove(v);
                }
                v++;
            }
        }

        //sort this in reverse order nvm
        for(int i=0; i< sortedExpox.size(); i++)
        {
            int currentElement = Integer.valueOf( sortedExpox.get(i).substring( sortedExpox.get(i).lastIndexOf("^")+1 ) );
            int currentIndex = i;
            for(int j = i; j > 0; j--)
            {
                if( currentElement > Integer.valueOf( sortedExpox.get(j-1).substring( sortedExpox.get(j-1).lastIndexOf("^")+1 ) ) )
                {
                    String itemToRight = sortedExpox.get(j-1) ;
                    sortedExpox.set(j - 1, sortedExpox.get(i));
                    sortedExpox.set(j, itemToRight);
                }
            }
        }

        ArrayList<String> sortedExpoy = new ArrayList<String>();
        for(int i = expoy.size()-1; i>=0; i--)
        {
            String l = expoy.get(i);
            for(int j = i-1; j>=0; j--)
            {
                if( expoy.get(i).substring( expoy.get(i).indexOf("^") ).equals( expoy.get(j).substring( expoy.get(j).indexOf("^") ) ) )
                {
                    l += " " + expoy.get(j);
                }
            }
            sortedExpoy.add(l);
        }
        for( int i= 0; i<sortedExpoy.size(); i++)
        {
            String l = sortedExpoy.get(i);
            int v = i+1;

            while( v < sortedExpoy.size() )
            {
                if( l.contains( sortedExpoy.get(v) ) )
                {
                    sortedExpoy.remove(v);
                }
                v++;
            }
        }

        for(int i=0; i< sortedExpoy.size(); i++)
        {
            int currentElement = Integer.valueOf( sortedExpoy.get(i).substring( sortedExpoy.get(i).lastIndexOf("^")+1 ) );
            int currentIndex = i;
            for(int j = i; j > 0; j--)
            {
                if( currentElement > Integer.valueOf( sortedExpoy.get(j-1).substring( sortedExpoy.get(j-1).lastIndexOf("^")+1 ) ) )
                {
                    String itemToRight = sortedExpoy.get(j-1) ;
                    sortedExpoy.set(j - 1, sortedExpoy.get(i));
                    sortedExpoy.set(j, itemToRight);
                }
            }
        }

        String last = "";
        int count = 0;
        for(int i=0; i<sortedExpox.size(); i++)
        {
            int coffeceint =0;
            ArrayList<String> finalexpo = new ArrayList<String>();
            StringTokenizer tokenizer2 = new StringTokenizer( sortedExpox.get(i) , " ", false);
            while(tokenizer2.hasMoreTokens())
            {
                finalexpo.add(tokenizer2.nextToken());
            }
            String expo = "";
            for(int j=0; j < finalexpo.size(); j++)
            {
                int jindex = finalexpo.get(j).indexOf("^");
                expo = finalexpo.get(j).substring(jindex+1);
                if( finalexpo.get(j).substring(0,jindex-1).equals("") ||  finalexpo.get(j).substring(0,jindex-1).equals("+") )
                {coffeceint += 1; }
                else if( finalexpo.get(j).substring(0,jindex-1).equals("-"))
                { coffeceint += -1;}
                else
                { coffeceint += Integer.valueOf( finalexpo.get(j).substring(0, jindex-1) ); }

            }

            if(coffeceint == 0)
            {

            }
            else if(count == 0)
            {
                last += coffeceint + "x<html><sup>"+expo+"</sup></html>";
                count++;
            }
            else if(coffeceint > 0)
            {
                last += "+"+coffeceint+"x<html><sup>"+expo+"</sup></html>";
            }
            else
            {
                last += coffeceint+"x<html><sup>"+expo+"</sup></html>";
            }
        }

        for(int i=0; i<sortedExpoy.size(); i++)
        {
            int coffeceint =0;
            ArrayList<String> finalexpo = new ArrayList<String>();
            StringTokenizer tokenizer2 = new StringTokenizer( sortedExpoy.get(i) , " ", false);
            while(tokenizer2.hasMoreTokens())
            {
                finalexpo.add(tokenizer2.nextToken());
            }
            String expo = "";
            for(int j=0; j < finalexpo.size(); j++)
            {
                int jindex = finalexpo.get(j).indexOf("^");
                expo = finalexpo.get(j).substring(jindex+1);
                if( finalexpo.get(j).substring(0,jindex-1).equals("") ||  finalexpo.get(j).substring(0,jindex-1).equals("+") )
                {coffeceint += 1; }
                else if( finalexpo.get(j).substring(0,jindex-1).equals("-"))
                { coffeceint += -1;}
                else
                {coffeceint += Integer.valueOf(finalexpo.get(j).substring(0, jindex - 1));}
            }
            if(coffeceint == 0)
            {}
            else if(count == 0)
            {
                last += coffeceint+"y<html><sup>"+expo+"</sup></html>";
                count++;
            }
            else if(coffeceint > 0)
            {
                last += "+"+coffeceint+"y<html><sup>"+expo+"</sup></html>";
            }
            else
            {
                last +=coffeceint+"y<html><sup>"+expo+"</sup></html>";
            }
        }

        int coffecientx = 0;
            for(int i=0; i< varx.size(); i++)
        {
            int xindex = varx.get(i).indexOf("x");
            if( varx.get(i).substring(0,xindex).equals("") || varx.get(i).substring(0,xindex).equals("+") )
            {coffecientx += 1; }
            else if( varx.get(i).substring(0,xindex).equals("-"))
            { coffecientx += -1;}
            else
            { coffecientx += Integer.valueOf( varx.get(i).substring(0,xindex) ); }
        }

            if(coffecientx == 0)
        {}
            else if(count==0)
        {
            last += coffecientx + "x";
            count++;
        }
            else if(coffecientx > 0)
        {
            last += "+"+coffecientx + "x";
        }
            else
        {
            last += coffecientx + "x";
        }

        int coffecienty = 0;
            for(int i=0; i< vary.size(); i++)
        {
            int yindex = vary.get(i).indexOf("y");
            if( vary.get(i).substring(0,yindex).equals("") ||  vary.get(i).substring(0,yindex).equals("+") )
            {coffecientx += 1; }
            else if(  vary.get(i).substring(0,yindex).equals("-"))
            { coffecientx += -1;}
            else
            { coffecienty += Integer.valueOf( vary.get(i).substring(0,yindex) ); }
        }

            if(coffecienty == 0)
        {}
            else if(count==0)
        {
            last += coffecienty + "y";
            count++;
        }
            else if(coffecienty > 0)
        {
            last += "+"+coffecienty + "y";
        }
            else
        {
            last += coffecienty + "y";
        }

        int constant =0;
            for(int i=0; i<not.size(); i++)
        {
            constant += Integer.valueOf(not.get(i));
        }

            if(constant == 0)
        {}
            else if(count == 0)
        {
            last += constant;
            count++;
        }
            else if(constant > 0)
        {
            last += "+"+constant;
        }
            else
        {
            last += constant;
        }

            return last;
    }

}

/*public void OnClick0(View view)
    {
        presentEquation += 0;
        backgroundEquation += 0;
        Update();
    }
    public void OnClick1(View view)
    {
        presentEquation += 1;
        backgroundEquation += 1;
        Update();
    }
    public void OnClick2(View view)
    {
        presentEquation += 2;
        backgroundEquation += 2;
        Update();
    }
    public void OnClick3(View view)
    {
        presentEquation += 3;
        backgroundEquation += 3;
        Update();
    }
    public void OnClick4(View view)
    {
        presentEquation += 4;
        backgroundEquation += 4;
        Update();
    }
    public void OnClick5(View view)
    {
        presentEquation += 5;
        backgroundEquation += 5;
        Update();
    }
    public void OnClick6(View view)
    {
        presentEquation += 6;
        backgroundEquation += 6;
        Update();
    }
    public void OnClick7(View view)
    {
        presentEquation += 7;
        backgroundEquation += 7;
        Update();
    }
    public void OnClick8(View view)
    {
        presentEquation += 8;
        backgroundEquation += 8;
        Update();
    }
    public void OnClick9(View view)
    {
        presentEquation += 9;
        backgroundEquation += 9;
        Update();
    }
 */
