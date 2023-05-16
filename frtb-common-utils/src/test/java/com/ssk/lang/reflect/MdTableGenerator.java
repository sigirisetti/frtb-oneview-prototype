package com.ssk.lang.reflect;

import org.apache.commons.lang3.StringUtils;

public class MdTableGenerator {

	private static final String mtTable_p1 = "<md-table-container>" + "<table md-table md-row-select>"
			+ "<thead md-head md-order=\"query.order\">" + "<tr md-row> <th md-column><span>Validation Message</span></th>";

	private static final String mtTable_p2 = "<th md-column md-order-by=\"nameToLower\"><span>Trade ID</span></th>"
			+ "<th md-column><span>TimeStamp</span></th>" + "<th md-column>Index name</th>"
			+ "<th md-column>Currency</th>" + "<th md-column>3M</th>" + "<th md-column>6M</th>"
			+ "<th md-column>1Y</th>" + "<th md-column>2Y</th>" + "<th md-column>3Y</th>" + "<th md-column>5Y</th>"
			+ "<th md-column>7Y</th>" + "<th md-column>10Y</th>" + "<th md-column>15Y</th>" + "<th md-column>30Y</th>";
	private static final String mtTable_p3 = "</tr>" + "</thead>" + "<tbody md-body>"
			+ "<tr md-row ng-repeat=\"row in irDeltaList\"> <td md-cell><span style=\"background-color: #FFA500;\">{{row.messages[0]}}</span></td>";
	private static final String mtTable_p4 = "<td md-cell>{{row.tradeId}}</td>" + "<td md-cell>{{row.timestamp}}</td>"
			+ "<td md-cell>{{row.indexName}}</td>" + "<td md-cell>{{row.currency}}</td>" + "<td md-cell>{{row.3m}}</td>"
			+ "<td md-cell>{{row.6m}}</td>" + "<td md-cell>{{row.1y}}</td>" + "<td md-cell>{{row.2y}}</td>"
			+ "<td md-cell>{{row.3y}}</td>" + "<td md-cell>{{row.5y}}</td>" + "<td md-cell>{{row.7y}}</td>"
			+ "<td md-cell>{{row.10y}}</td>" + "<td md-cell>{{row.15y}}</td>" + "<td md-cell>{{row.30y}}</td>";

	private static final String mtTable_p5 = "</tr>" + "</tbody>" + "</table>" + "</md-table-container>";

	public static void main(String[] args) {
		String[] irVega = { "Trade ID", "TimeStamp", "PO", "Product", "Desk", "Book", "Location" };
		System.out.println(mtTable_p1);
		System.out.println("<th md-column md-order-by=\"nameToLower\"><span>Trade ID</span></th>");
		System.out.println("<th md-column><span>TimeStamp</span></th>");
		for (int i = 2; i < irVega.length; i++) {
			System.out.println(String.format("<th md-column>%s</th>", irVega[i]));
		}
		System.out.println(mtTable_p3);
		for (int i = 0; i < irVega.length; i++) {
			if(irVega[i].startsWith("_")) {
				System.out.println(String.format("<td md-cell>{{row._%s}}</td>", StringUtils.capitalize(irVega[i])));
			}else {
				System.out.println(String.format("<td md-cell>{{row.%s}}</td>", StringUtils.capitalize(irVega[i])));
			}
		}
		System.out.println(mtTable_p5);

	}
}
