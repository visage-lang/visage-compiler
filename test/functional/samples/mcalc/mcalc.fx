import javafx.ui.*;
import java.lang.Math;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.lang.System;

/**
 * @test
 *
 * Sample of various ui element bindings; this has no animations, but it's useful.
 * A mortgage and amortization calculator. 
 * Some functionality does not fully work due to apparent bugs in javfx.ui.
 *
 * Below bug seems to be fixed! 
 * BUG: Down Payment % does not cause ui updates. This works with javafx.gui libraries but not with javafx.ui
 * Same bug shows up on page 2 with the yearly interest, but javafx.gui has no table support so I can't complete
 * this application with javafx.gui.
 * See mcalc_bug for simple repeatable version of bug.
 *
 * 6/05/08 - Added a page3 with saved loans. It would be nice to have a table that can be sorted.
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
var table:Table;
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
/** Fonts for ui elements */
var font1 = Font { size: 18 face: FontFace.BROADWAY }
var font2 = Font { size: 14 face: FontFace.GEORGIA style: FontStyle.BOLD }

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
 * Page1 UI components, SimpleLabels and Textfields 
 * Edittable TextFields bind to String version of loan variables.
 * Read only TextFields bind to formatNumber() call.
 */
//row 1
var applicationTitle = SimpleLabel{text: "Easy Mortgage Calculator" font: font1 };
//row 2
var termLabel = SimpleLabel{font: font2 text:"Term: "};
var termTextField = TextField{background: Color.WHITE columns:3 value: bind Term with inverse};
var interestRateLabel = SimpleLabel{font: font2 text:"Interest Rate % "};
var interestRateTextField = TextField{background: Color.WHITE columns:4 value: bind InterestRate with inverse};
//row3
var purchasePriceLabel = SimpleLabel{font: font2 text:"Purchase Price $"};
var purchasePriceTextField = TextField{background: Color.WHITE columns:8 value: bind PurchasePrice with inverse};
//row4
var downPaymentLabel = SimpleLabel{font: font2 text:"Down Payment %"};
var downPercentTextField = TextField{background: Color.WHITE columns:3 value: bind DownPercent with inverse};
var downAmountTextField = TextField{background: Color.WHITESMOKE columns:8 editable: false value: bind formatNumber(DownAmount,1,2)};
//row5
var principleLabel = SimpleLabel{font: font2 text:"Principle "};
var principleTextField = TextField{background: Color.WHITESMOKE columns:10 editable: false value: bind formatNumber(Principle,1,2)};
//row6
var propTaxLabel = SimpleLabel{ font: font2 text: "Tax Rate%" };
var propTaxTextField = TextField {background: Color.WHITE columns:3 value: bind TaxRate with inverse};
var assessedLabel = SimpleLabel{ font: font2 text:"Assessed Value $"};
var assessedTextField = TextField{background: Color.WHITE columns:8 value: bind AssessedValue with inverse};
//row7
var monthlyPandILabel = SimpleLabel{font: font2 text:"Monthly P&I $"};
var monthlyPandITextField = TextField{background: Color.YELLOW columns:8 editable: false value: bind formatNumber(MonthlyPI,1,2)};
var SaveButton = Button{text:"Save" action: function(){saveThisMortgage();} };
var ViewSavedButton = Button{text:"View Saved" action: function(){	currentPage = 2} };
//row8
var monthlyRETaxLabel = SimpleLabel{font: font2 text:"Monthly R.E.Tax $"};
var monthlyRETaxTextField = TextField{background: Color.YELLOW columns:6 editable: false value: bind formatNumber(RETax,1,2)};
//row9
var yearlyInsuranceLabel = SimpleLabel{font: font2 text:"Yearly Insurance $"};
var yearlyInsuranceTextField = TextField{background: Color.WHITE columns:6 value: bind Insurance with inverse};
//row10
var MonthlyPaymentLabel = SimpleLabel{font: font2 text:"Montly Payments"};
var MonthlyPaymentTextField = TextField{background: Color.YELLOW columns:8 editable: false value: bind formatNumber(MonthlyPymt,1,2)};
//row11 - button to generate amortization table, show next page.
var updateButton = Button { font: font2 text:"Amortization Table" action: function() {
   atable = GetAmortizationTable(Principle, NUM.parseDouble(InterestRate), INT.parseInt(Term), MonthlyPI);
	var m = 1;
	currentPage = 1
} };
var ExitButton = Button{text:"Exit" action: function(){java.lang.System.exit(0)} };

/** current page index for changing between screens */
var currentPage = 0;

/** Loan Information Page Layout placing above elements */
var Page1Row1  = FlowPanel {content: applicationTitle};
var Page1Row2  = FlowPanel {alignment: Alignment{name:"left"} content: [termLabel,termTextField,interestRateLabel,interestRateTextField]};
var Page1Row3  = FlowPanel {alignment: Alignment{name:"left"} content: [purchasePriceLabel,purchasePriceTextField]};
var Page1Row4  = FlowPanel {alignment: Alignment{name:"left"} content: [downPaymentLabel,downPercentTextField,downAmountTextField]};
var Page1Row5  = FlowPanel {alignment: Alignment{name:"left"} content: [principleLabel,principleTextField]};
var Page1Row6  = FlowPanel {alignment: Alignment{name:"left"} content: [propTaxLabel,propTaxTextField,assessedLabel,assessedTextField]};
var Page1Row7  = FlowPanel {alignment: Alignment{name:"left"} content: [monthlyPandILabel,monthlyPandITextField,SaveButton,ViewSavedButton]};
var Page1Row8  = FlowPanel {alignment: Alignment{name:"left"} content: [monthlyRETaxLabel,monthlyRETaxTextField]};
var Page1Row9  = FlowPanel {alignment: Alignment{name:"left"} content: [yearlyInsuranceLabel,yearlyInsuranceTextField]};
var Page1Row10 = FlowPanel {alignment: Alignment{name:"left"} content: [MonthlyPaymentLabel,MonthlyPaymentTextField]};
var Page1Row11 = FlowPanel {alignment: Alignment{name:"left"} content: [updateButton,ExitButton]};

var Page1 = GridPanel {
	background: Color.LIGHTGREEN
	rows: 11
	columns: 1 
	cells: [Page1Row1,Page1Row2,Page1Row3,Page1Row4,Page1Row5,Page1Row6,Page1Row7,Page1Row8,Page1Row9,Page1Row10,Page1Row11]
};

/** 
 * Amortization Page 
 */

/**
 * Data structure to hold saved mortagages
 *
 */
 class amortgage {
   attribute tempTerm = "30";
	attribute tempIRate = "0.0";
	attribute tempPurchasePrice = "0";
	attribute tempDownPercent = "0.0";
	attribute tempPrinciple = 0;
	attribute tempMonthlyPI = 0.0;
	function compareTo(m:amortgage):Boolean {
		 var isSame=false;
		 if(tempTerm.compareTo(m.tempTerm)==0 and 
			 tempIRate.compareTo(m.tempIRate)==0 and
			 tempPurchasePrice.compareTo(m.tempPurchasePrice)==0 and
			 tempDownPercent.compareTo(m.tempDownPercent)==0){ isSame=true; }
			return isSame;
	}
}
var savedMortgages:amortgage[];
function saveThisMortgage() {
	var tempMortgage = amortgage {
		 tempTerm:Term;
		 tempIRate:InterestRate;
		 tempPurchasePrice:PurchasePrice;
		 tempDownPercent:DownPercent;
		 tempPrinciple:Principle;
		 tempMonthlyPI:MonthlyPI;
	}
	var isSaved = false;
	for(M in savedMortgages) {
	  if( M.compareTo(tempMortgage) ) { isSaved = true; }
	}
	if( isSaved <> true) { 	insert tempMortgage into savedMortgages; }
	else { System.out.println("Mortgage is already saved."); }
}


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
var summaryLabel = SimpleLabel{text: "Mortgage Summary" font: font1 };
var Page2Row1 = FlowPanel {content: summaryLabel};
/** TERM */
var Page2Row2 = FlowPanel {
alignment: Alignment{name:"left"}
content: [SimpleLabel{font: font2 text:"Term "},SimpleLabel{background: Color.WHITESMOKE width:50 text: bind Term.toString()},
          SimpleLabel{font: font2 text:"Interest Rate  %"},SimpleLabel{background: Color.WHITESMOKE width:10 text: bind InterestRate.toString()}
			]};
/** PURCHASE PRICE */
var Page2Row3 = FlowPanel {alignment: Alignment{name:"left"}
                      content: [SimpleLabel{font: font2 text:"Purchase Price: $"},
                                SimpleLabel{background: Color.WHITESMOKE width:10 text: bind formatNumber(INT.parseInt(PurchasePrice),1,2)}]};
/** DOWN PAYMENT */
var Page2Row4 = FlowPanel {alignment: Alignment{name:"left"}
                      content: [SimpleLabel{font: font2 text:"Down Payment %"},
                                SimpleLabel{background: Color.WHITESMOKE width:10 text: bind DownPercent.toString()},
                                SimpleLabel{font: font2 text:"   $"},
                                SimpleLabel{background: Color.WHITESMOKE width:10 text: bind formatNumber(DownAmount,1,2)}]};
/** PRINCIPLE & PROPERTY TAX */
var Page2Row5 = FlowPanel {alignment: Alignment{name:"left"}
                      content: [SimpleLabel{font: font2 text:"Principle $ "},
                                SimpleLabel{background: Color.WHITESMOKE width:10 text: bind formatNumber(Principle,1,2)},
										  SimpleLabel{font: font2 text:"Property Tax: "},
										  SimpleLabel{background: Color.WHITESMOKE width:8 text: bind formatNumber(RETax,1,2)}
										  ]};
/** MONTHLY P & I */
var Page2Row6 = FlowPanel {alignment: Alignment{name:"left"}
                        content: [SimpleLabel{font: font2 text:"Montly P&I: "},
                                SimpleLabel{background: Color.WHITESMOKE width:10 text: bind formatNumber(MonthlyPI,1,2)}]};
/** BUTTONS */
var Page2Row7 = FlowPanel {alignment: Alignment{name:"center"}
       content: [ Button{font: font2 text:"Back" action: function(){currentPage = 0; }},
						Button{font: font2 text:"Exit" action: function(){java.lang.System.exit(0);}} ]
};

/** Interest, total interest and total payment information atop amortization table */

/** Interest for a specified year */
var interestYear = "0";
var yearlyInterestLabel = SimpleLabel{ font: font2 text: "Interest for year" };
var yearTextField = TextField {background: Color.WHITE columns:2 value: bind interestYear with inverse};
var yearlyInterestTextField = TextField {background: Color.YELLOW columns:7 editable: false  value: bind formatNumber(GetYearlyInterest(interestYear),1,0)};
var Page2Row8 = FlowPanel {alignment: Alignment{name:"left"} content: [yearlyInterestLabel,yearTextField,yearlyInterestTextField]};
/** total interest and total payments for life of loan */
var Page2Row9 = FlowPanel {alignment: Alignment{name:"left"}
                      content: [SimpleLabel{font: font2 text:"Ttl Interest"},
                                SimpleLabel{background: Color.WHITESMOKE width:10 text: bind formatNumber(totalInterest,1,0)},
										  SimpleLabel{font: font2 text:"Ttl Payments"},
										  SimpleLabel{background: Color.WHITESMOKE width:8 text: bind formatNumber(totalPayments,1,0)}
							 ]};

/** Mortgage Information - top half of 2nd page */
var mortgageSummary = GridPanel {
	background: Color.LIGHTGREEN
	rows: 9 
	columns: 1 
	cells: [Page2Row1,Page2Row2,Page2Row3,Page2Row4,Page2Row5,Page2Row6,Page2Row7,Page2Row8,Page2Row9]
};

//  0, 1, 2, 3, 4   5, 6, 7, 8, 9 
// 10,11,12,13,14  15,16,17,18,19
// 20,21,22,23,24  25,26,27,28,29

/** A way to alternate row colors on the amortization table*/
var CCnt = 0;  
function getTCbg():Color { if (CCnt++ % 10 < 5) {return Color.WHITE;} return Color.WHITESMOKE;}

/** Page2 has mortgage summary and amortization table */
var Page2 = BorderPanel {
   top: mortgageSummary
	center:  table = Table {
		columns: [
		TableColumn{alignment: HorizontalAlignment.CENTER width: 32 text:"Mo"},
		TableColumn{alignment: HorizontalAlignment.CENTER width: 48 text:"Payment"},
		TableColumn{alignment: HorizontalAlignment.CENTER width: 48 text:"Principle"},
		TableColumn{alignment: HorizontalAlignment.CENTER width: 48 text:"Interest"},
		TableColumn{alignment: HorizontalAlignment.CENTER width: 48 text:"Balance"}
		]
		cells: bind for (arow in atable) [
		TableCell{background: getTCbg() text: bind (arow.month).toString()  },
		TableCell{background: getTCbg() text: bind formatNumber((arow.payment as Integer),1,0)  },
		TableCell{background: getTCbg() text: bind formatNumber((arow.principle as Integer),1,0)},
		TableCell{background: getTCbg() text: bind formatNumber((arow.interest as Integer),1,0) },
		TableCell{background: getTCbg() text: bind formatNumber((arow.balance as Integer),1,0)  }
		]
		}
}

var saveLoansLabel = SimpleLabel{text: "Save Loans" font: font1 };
var Page3Row1 = FlowPanel {content: saveLoansLabel};
var loansSummary = GridPanel {
	background: Color.LIGHTGREEN
	rows: 1 
	columns: 1 
	cells: [Page3Row1]
};


var Page3 = BorderPanel {
   top: loansSummary
	center:  table = Table {
		columns: [
		TableColumn{alignment: HorizontalAlignment.CENTER width: 24 text:"Term"},
		TableColumn{alignment: HorizontalAlignment.CENTER width: 24 text:"IntRate"},
		TableColumn{alignment: HorizontalAlignment.CENTER width: 32 text:"PurchasePrice"},
		TableColumn{alignment: HorizontalAlignment.CENTER width: 24 text:"DownPercent"},
		TableColumn{alignment: HorizontalAlignment.CENTER width: 32 text:"Principle"},
		TableColumn{alignment: HorizontalAlignment.CENTER width: 48 text:"Monthly P&I"}
		]
		cells: bind for (arow in savedMortgages) [
		TableCell{background: getTCbg() text: bind (arow.tempTerm)  },
		TableCell{background: getTCbg() text: bind (arow.tempIRate)  },
		TableCell{background: getTCbg() text: bind formatNumber((INT.parseInt(arow.tempPurchasePrice)),1,0)},
		TableCell{background: getTCbg() text: bind (arow.tempDownPercent) },
		TableCell{background: getTCbg() text: bind formatNumber((arow.tempPrinciple as Integer),1,0)  },
		TableCell{background: getTCbg() text: bind formatNumber((arow.tempMonthlyPI as Integer),1,0) }
		]
		}
	bottom: FlowPanel {
		background: Color.LIGHTGREEN
	    alignment: Alignment{name:"center"}
       content: [ Button{font: font2 text:"Back" action: function(){currentPage = 0; }},
						Button{font: font2 text:"Exit" action: function(){java.lang.System.exit(0);}} ]
	}
}


/** CardPanel and Application Frame */
var cardPanel = CardPanel {
    cards: [Page1,Page2,Page3]
    selection: bind currentPage
};

Frame {
    title: "Mortgage Calculator"
    content: cardPanel
    visible: true
    height: 575
    width: 400
}

