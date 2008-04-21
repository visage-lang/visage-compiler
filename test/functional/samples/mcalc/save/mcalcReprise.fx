import javafx.gui.*;
import javafx.ui.Table;
import javafx.ui.CardPanel;
import javafx.ui.TableColumn;
import javafx.ui.TableCell;

import java.lang.Math;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.lang.System;
import javax.swing.JComponent;

/*
 * javafx.gui version - .gui is missing some pieces so page2, the amortization
 *                      table is not available.
 */

/**
 * Loan Information 
 */

/** Integer and Number instances so I can use static functions ( like INT.parseInt() ) */
var INT = 0;
var NUM = 1.0;
/**
 * Values are strings to enable ui binding, translated to values for computation
 * Start with some typical SF Bay Area numbers.
 */
var Term = "30";
var PurchasePrice = "650000";
var DownPercent = "10";
var InterestRate = "6.25";
var TaxRate    = "1.25";
/** Start with purchase price, but not bound as assessed value usually differs after purchase */
var AssessedValue = "650000"; 
var Insurance = "1200";
/** Holds generated amortization table */
var atable:amortTableRow[];
/** ifdef Table widget 
var table:Table;  
*/
/** Bound numeric variables for doing computations */
var DownAmount  = bind (INT.parseInt(PurchasePrice) * INT.parseInt(DownPercent))/100;
var Principle   = bind INT.parseInt(PurchasePrice) - DownAmount ; 
var Fi_to_n     = bind Math.pow(NUM.parseDouble(InterestRate) / 100.0 / 12.0 + 1.0, -(INT.parseInt(Term) * 12.0));
var MonthlyPI   = bind ((Principle * (NUM.parseDouble(InterestRate) / 100.0 / 12.0)) / (1.0 - Fi_to_n));
var RETax		 = bind ((INT.parseInt(AssessedValue) * NUM.parseDouble(TaxRate)) / 100.0 / 12.0);
var MonthlyPymt = bind MonthlyPI + RETax + INT.parseInt(Insurance)/12.0;
/** Global variables */
var totalInterest = 0.0;
var totalPayments = 0.0;
var PayoffMonth = 0;
// not sure what this is for? :)
var InterestDiff = 0.0;
/** This may need a string version to put in UI */
var additionalPrinciple = 0;
var table : Table;
var font1 = Font.font("Verdana", FontStyle.BOLD, 14);
/**
 * TODO: For new home owners, should add PMI costs to UI.
 */
//var PMIRate = "0.5";
//var MoPMI = bind (Principle * (NUM.parseDouble(PMIRate)/100.0) / 12.0);

/**
 * Format Numbers as dollar value Strings
 * @parm id Minimum number of integer digits..usually 1
 * @parm fd Minimum number of fraction digits
 */
var df:DecimalFormat = new DecimalFormat("$###,###.##");
function formatNumber(n:Number, id:Integer, fd:Integer):String {
   df.setMinimumIntegerDigits(id);
	df.setMinimumFractionDigits(fd);
   return df.format(n);
 }

/**
 * Page1 UI components, Labels and Textfields 
 * Edittable TextFields bind to String version of loan variables.
 * Read only TextFields bind to formatNumber() call.
 */
//row 1
var applicationTitle = Label{font: font1 text: "Easy Mortgage Calculator"  };
//row 2
var termLabel = Label{text:"Term: "};
var termTextField = TextField{background: Color.WHITE columns:3 text: bind Term with inverse};
var interestRateLabel = Label{text:"Interest Rate % "};
var interestRateTextField = TextField{background: Color.WHITE columns:4 text: bind InterestRate with inverse};
//row3
var purchasePriceLabel = Label{ text:"Purchase Price $"};
var purchasePriceTextField = TextField{background: Color.WHITE columns:8 text: bind PurchasePrice with inverse};
//row4
var downPaymentLabel = Label{ text:"Down Payment %"};
var downPercentTextField = TextField{background: Color.WHITE columns:3 text: bind DownPercent with inverse};
var downAmountTextField = TextField{columns:8 editable: false text: bind formatNumber(DownAmount,1,2)};
//row5
var principleLabel = Label{ text:"Principle "};
var principleTextField = TextField{columns:10 editable: false text: bind formatNumber(Principle,1,2)};
//row6
var propTaxLabel = Label{  text: "Tax Rate%" };
var propTaxTextField = TextField {background: Color.WHITE columns:3 text: bind TaxRate with inverse};
var assessedLabel = Label{  text:"Assessed Value $"};
var assessedTextField = TextField{background: Color.WHITE columns:8 text: bind AssessedValue with inverse};
//row7
var monthlyPandILabel = Label{ text:"Monthly P&I $"};
var monthlyPandITextField = TextField{background: Color.YELLOW columns:8 editable: false text: bind formatNumber(MonthlyPI,1,2)};
//row8
var monthlyRETaxLabel = Label{ text:"Monthly R.E.Tax $"};
var monthlyRETaxTextField = TextField{background: Color.YELLOW columns:6 editable: false text: bind formatNumber(RETax,1,2)};
//row9
var yearlyInsuranceLabel = Label{ text:"Yearly Insurance $"};
var yearlyInsuranceTextField = TextField{background: Color.WHITE columns:6 text: bind Insurance with inverse};
//row10
var MonthlyPaymentLabel = Label{ text:"Montly Payments"};
var MonthlyPaymentTextField = TextField{background: Color.YELLOW columns:8 editable: false text: bind formatNumber(MonthlyPymt,1,2)};
//row11 - button to generate amortization table, show next page.
var updateButton = Button {  text:"Amortization Table" action: function() {
   atable = GetAmortizationTable(Principle, NUM.parseDouble(InterestRate), INT.parseInt(Term), MonthlyPI);
	var m = 1;
	currentPage = 1
} };
var ExitButton = Button{text:"Exit" action: function(){java.lang.System.exit(0)} };

/** current page index for changing between screens */
var currentPage = 0;

/** Loan Information Page Layout placing above elements */
var Page1Row1  = FlowPanel { content: applicationTitle};
var Page1Row2  = FlowPanel { content: [termLabel,termTextField,interestRateLabel,interestRateTextField]};
var Page1Row3  = FlowPanel { content: [purchasePriceLabel,purchasePriceTextField]};
var Page1Row4  = FlowPanel { content: [downPaymentLabel,downPercentTextField,downAmountTextField]};
var Page1Row5  = FlowPanel { content: [principleLabel,principleTextField]};
var Page1Row6  = FlowPanel { content: [propTaxLabel,propTaxTextField,assessedLabel,assessedTextField]};
var Page1Row7  = FlowPanel { content: [monthlyPandILabel,monthlyPandITextField]};
var Page1Row8  = FlowPanel { content: [monthlyRETaxLabel,monthlyRETaxTextField]};
var Page1Row9  = FlowPanel { content: [yearlyInsuranceLabel,yearlyInsuranceTextField]};
var Page1Row10 = FlowPanel { content: [MonthlyPaymentLabel,MonthlyPaymentTextField]};
var Page1Row11 = FlowPanel { content: [updateButton,ExitButton]};

var Page1 = GridPanel {
	background: Color.LIGHTGREY
	rows: 11
	columns: 1 
	content: [Page1Row1,Page1Row2,Page1Row3,Page1Row4,Page1Row5,Page1Row6,Page1Row7,Page1Row8,Page1Row9,Page1Row10,Page1Row11]
};

/** 
 * Amortization Page 
 */

/**
 * Data structure for basic row unit in the amortization table
 */
class amortTableRow {
   attribute month     = 1;
	attribute payment   = 0.0;
	attribute interest  = 0.0;
	attribute principle = 0.0;
	attribute balance   = 0.0;
}

/** 
 * Generates amortization table(sequence) for given loan values 
 * @parm pPrincipal Loan amount
 * @parm pInterest Interest rate
 * @parm pTerm Term in years for the loan
 * @parm pMonthlyPandI Monthly principle and interest payment
 * @return Sequence of amortTableRow data structures
 */
function GetAmortizationTable(pPrincipal:Number, pInterest:Number, pTerm:Integer, pMonthlyPandI:Number) : amortTableRow[]    {
        var PrevBalance = pPrincipal;
        var IRate = pInterest / 100.0 / 12.0;
        var MONTHS = pTerm * 12;
        //variable for accumulating values
		  totalInterest = 0.0;
        totalPayments = 0.0;

        var A_Table : amortTableRow[];
        var month = -1;
        while(  month < MONTHS and PrevBalance >= 0.0)
        {	month++;
		      var tr = new amortTableRow;
				tr.month=month+1;
				tr.payment = pMonthlyPandI + additionalPrinciple;
				tr.interest = IRate * PrevBalance;
				tr.principle =  (pMonthlyPandI - IRate * PrevBalance) + additionalPrinciple;
            if(month == MONTHS - 1) {
					 tr.balance = 0.0;
				}
            else {
					tr.balance =  PrevBalance - ((pMonthlyPandI - IRate * PrevBalance) + additionalPrinciple);
				}
            insert tr into A_Table;

				PrevBalance -= (pMonthlyPandI - IRate * PrevBalance) + additionalPrinciple;
            totalInterest += IRate * PrevBalance;
            totalPayments += pMonthlyPandI + additionalPrinciple;
            if(PrevBalance > 0.0) {  continue; }

				PayoffMonth = month;
            break;
        }
		  return A_Table;
    }

/**
 * Compute interest to be paid for a given year
 * @parm yeer The year for which to compute interest
 * @return Total interest to be paid for the given year
 */
function GetYearlyInterest( yeer:String ):Number    {
        var yearlyInt = 0.0;
		  var year = INT.parseInt(yeer);
		  var start = year * 12 - 12;
		  var end = year*12-1;
		  if(end > sizeof atable) { return yearlyInt;}
        for( I in [start..end]) { yearlyInt += atable[I].interest; }
        return yearlyInt;
}

/** Loan Payments Page Layout */
var summaryLabel = Label{text: "Mortgage Summary"  };
var Page2Row1 = FlowPanel {content: summaryLabel};
/** TERM */
var Page2Row2 = FlowPanel {

content: [Label{ text:"Term "},Label{width:50 text: bind Term.toString()},
          Label{ text:"Interest Rate  %"},Label{width:10 text: bind InterestRate.toString()}
			]};
/** PURCHASE PRICE */
var Page2Row3 = FlowPanel {
                      content: [Label{ text:"Purchase Price: $"},
                                Label{width:10 text: bind formatNumber(INT.parseInt(PurchasePrice),1,2)}]};
/** DOWN PAYMENT */
var Page2Row4 = FlowPanel {
                      content: [Label{ text:"Down Payment %"},
                                Label{width:10 text: bind DownPercent.toString()},
                                Label{ text:"   $"},
                                Label{width:10 text: bind formatNumber(DownAmount,1,2)}]};
/** PRINCIPLE & PROPERTY TAX */
var Page2Row5 = FlowPanel {
                      content: [Label{ text:"Principle $ "},
                                Label{width:10 text: bind formatNumber(Principle,1,2)},
										  Label{ text:"Property Tax: "},
										  Label{width:8 text: bind formatNumber(RETax,1,2)}
										  ]};
/** MONTHLY P & I */
var Page2Row6 = FlowPanel {
                        content: [Label{ text:"Montly P&I: "},
                                Label{width:10 text: bind formatNumber(MonthlyPI,1,2)}]};
/** BUTTONS */
var Page2Row7 = FlowPanel {
       content: [ Button{ text:"Back" action: function(){currentPage = 0; }},
						Button{ text:"Exit" action: function(){java.lang.System.exit(0);}} ]
};

/** Interest, total interest and total payment information atop amortization table */

/** Interest for a specified year */
var interestYear = "0";
var yearlyInterestLabel = Label{  text: "Interest for year" };
var yearTextField = TextField {background: Color.WHITE columns:2 text: bind interestYear with inverse};
var yearlyInterestTextField = TextField {background: Color.YELLOW columns:7 editable: false  text: bind formatNumber(GetYearlyInterest(interestYear),1,0)};
var Page2Row8 = FlowPanel { content: [yearlyInterestLabel,yearTextField,yearlyInterestTextField]};
/** total interest and total payments for life of loan */
var Page2Row9 = FlowPanel {
                      content: [Label{ text:"Ttl Interest"},
                                Label{width:10 text: bind formatNumber(totalInterest,1,0)},
										  Label{ text:"Ttl Payments"},
										  Label{width:8 text: bind formatNumber(totalPayments,1,0)}
							 ]};

/** Mortgage Information - top half of 2nd page */
var mortgageSummary = GridPanel {
	background: Color.LIGHTGREY
	rows: 9 
	columns: 1 
	content: [Page2Row1,Page2Row2,Page2Row3,Page2Row4,Page2Row5,Page2Row6,Page2Row7,Page2Row8,Page2Row9]
};

/** Page2 has mortgage summary and amortization table */
var Page2 = BorderPanel {
   top: mortgageSummary
/*
	center:  table = Table {
		columns: [
		TableColumn{alignment: javafx.ui.HorizontalAlignment.CENTER width: 32 text:"Mo"},
		TableColumn{alignment: javafx.ui.HorizontalAlignment.CENTER width: 48 text:"Payment"},
		TableColumn{alignment: javafx.ui.HorizontalAlignment.CENTER width: 48 text:"Principle"},
		TableColumn{alignment: javafx.ui.HorizontalAlignment.CENTER width: 48 text:"Interest"},
		TableColumn{alignment: javafx.ui.HorizontalAlignment.CENTER width: 48 text:"Balance"}
		]
		cells: bind for (arow in atable) [
		TableCell{background: javafx.ui.Color.WHITESMOKE text: bind (arow.month).toString()  },
		TableCell{background: javafx.ui.Color.WHITESMOKE text: bind formatNumber((arow.payment as Integer),1,0)  },
		TableCell{background: javafx.ui.Color.WHITESMOKE text: bind formatNumber((arow.principle as Integer),1,0)},
		TableCell{background: javafx.ui.Color.WHITESMOKE text: bind formatNumber((arow.interest as Integer),1,0) },
		TableCell{background: javafx.ui.Color.WHITESMOKE text: bind formatNumber((arow.balance as Integer),1,0)  }
		]
		}
//*/
}

/** CardPanel and Application Frame */
/* No CardPanel widget...will there be one?
var cardPanel = CardPanel {
    cards: [Page1,Page2]
    selection: bind currentPage
};
//*/
Frame {
    title: "Mortgage Calculator"
    content: Page1
    visible: true
    height: 450
    width: 400
}
