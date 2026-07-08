package ir.ac.kntu.util;

import ir.ac.kntu.main.LibraryManger;
import ir.ac.kntu.modules.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class HtmlReportGenerator {

    private HtmlReportGenerator() {
    }

    public static void generateReport(String fileName) throws IOException {

        Catalog catalog = Catalog.getInstance();
        LibraryManger manager = LibraryManger.getInstance();
        List<LibraryItem> items = catalog.getItems();

        int books = 0;
        int magazines = 0;
        int ebooks = 0;
        int audioBooks = 0;

        Map<String, Integer> categories = new HashMap<>();

        for (LibraryItem item : items) {
            if (item instanceof Book) {
                books++;
            } else if (item instanceof Magazine) {
                magazines++;
            } else if (item instanceof Ebook) {
                ebooks++;
            } else if (item instanceof AudioBook) {
                audioBooks++;
            }
            categories.merge(item.getCategory(), 1, Integer::sum);
        }

        int students = 0;
        int faculty = 0;
        int guests = 0;

        for (NormalUser user : manager.getUserById().values()) {
            if (user instanceof Student) {
                students++;
            } else if (user instanceof Faculty) {
                faculty++;
            } else if (user instanceof Guest) {
                guests++;
            }
        }

        StringBuilder html = new StringBuilder();

        html.append("""
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Library Dashboard</title>

                    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

                    <style>

                        body{
                            font-family: Arial, sans-serif;
                            margin:40px;
                            background:#f4f4f4;
                        }

                        h1{
                            text-align:center;
                        }

                        h2{
                            text-align:center;
                        }

                        .section{
                            background:white;
                            padding:20px;
                            margin-bottom:30px;
                            border-radius:10px;
                        }

                        table{
                            width:100%;
                            border-collapse:collapse;
                            margin-top:15px;
                        }

                        th,td{
                            border:1px solid #cccccc;
                            padding:10px;
                            text-align:center;
                        }

                        th{
                            background:#eeeeee;
                        }

                        canvas{
                            max-width:700px;
                            margin:auto;
                            display:block;
                        }

                    </style>

                </head>

                <body>

                <h1>Library Dashboard</h1>
                """);

        html.append("<p><b>Total Users:</b> ").append(manager.getUserById().size()).append("</p>");

        html.append("<p><b>Total Items:</b> ").append(items.size()).append("</p>");

        html.append("""
                <div class="section">

                <h2>Library Resources Overview</h2>

                <table>

                    <tr>
                        <th>Books</th>
                        <th>Magazines</th>
                        <th>Ebooks</th>
                        <th>AudioBooks</th>
                    </tr>

                    <tr>
                """);

        html.append("<td>").append(books).append("</td>");

        html.append("<td>").append(magazines).append("</td>");

        html.append("<td>").append(ebooks).append("</td>");

        html.append("<td>").append(audioBooks).append("</td>");

        html.append("""
                    </tr>

                </table>

                <canvas id="resourceChart"></canvas>

                </div>
                """);

        html.append("""
                <div class="section">

                <h2>Category Distribution</h2>

                <table>

                    <tr>
                        <th>Category</th>
                        <th>Count</th>
                    </tr>
                """);

        for (Map.Entry<String, Integer> entry : categories.entrySet()) {

            html.append("<tr>");
            html.append("<td>").append(entry.getKey()).append("</td>");
            html.append("<td>").append(entry.getValue()).append("</td>");
            html.append("</tr>");
        }

        html.append("""
                </table>

                <canvas id="categoryChart"></canvas>

                </div>
                """);

        html.append("""
                <div class="section">

                <h2>User Role Distribution</h2>

                <table>

                    <tr>
                        <th>Students</th>
                        <th>Faculty</th>
                        <th>Guests</th>
                    </tr>

                    <tr>
                """);

        html.append("<td>").append(students).append("</td>");

        html.append("<td>").append(faculty).append("</td>");

        html.append("<td>").append(guests).append("</td>");

        html.append("""
                    </tr>

                </table>

                <canvas id="roleChart"></canvas>

                </div>
                """);

        html.append("""
                <div class="section">

                <h2>All Entities</h2>

                <table>

                    <tr>
                        <th>Users</th>
                        <th>Supporters</th>
                        <th>Admins</th>
                    </tr>

                    <tr>
                """);

        html.append("<td>").append(manager.getUserById().size()).append("</td>");
        html.append("<td>").append(manager.getSupporterByPassword().size()).append("</td>");
        html.append("<td>").append(manager.getAdminByPassword().size()).append("</td>");

        html.append("""
                    </tr>

                </table>

                <canvas id="entityChart"></canvas>

                </div>
                """);

        html.append("""
                <script>

                new Chart(
                    document.getElementById('resourceChart'),
                    {
                        type:'pie',
                        data:{
                            labels:[
                                'Books',
                                'Magazines',
                                'Ebooks',
                                'AudioBooks'
                            ],
                            datasets:[{
                                data:[
                """);

        html.append(books).append(",").append(magazines).append(",").append(ebooks).append(",").append(audioBooks);

        html.append("""
                                ]
                            }]
                        }
                    }
                );

                """);

        html.append("""
                new Chart(
                    document.getElementById('categoryChart'),
                    {
                        type:'bar',
                        data:{
                            labels:[
                """);

        boolean first = true;

        for (String category : categories.keySet()) {

            if (!first) {
                html.append(",");
            }

            html.append("'").append(category).append("'");

            first = false;
        }

        html.append("""
                            ],
                            datasets:[{
                                label:'Items',
                                data:[
                """);

        first = true;

        for (Integer count : categories.values()) {

            if (!first) {
                html.append(",");
            }

            html.append(count);

            first = false;
        }

        html.append("""
                                ]
                            }]
                        }
                    }
                );

                """);

        html.append("""
                new Chart(
                    document.getElementById('roleChart'),
                    {
                        type:'doughnut',
                        data:{
                            labels:[
                                'Students',
                                'Faculty',
                                'Guests'
                            ],
                            datasets:[{
                                data:[
                """);

        html.append(students).append(",").append(faculty).append(",").append(guests);

        html.append("""
                new Chart(
                    document.getElementById('entityChart'),
                    {
                        type:'pie',
                        data:{
                            labels:[
                                'Users',
                                'Supporters',
                                'Admins'
                            ],
                            datasets:[{
                                data:[
                """);

        html.append(manager.getUserById().size())
                .append(",")
                .append(manager.getSupporterByPassword().size())
                .append(",")
                .append(manager.getAdminByPassword().size());

        html.append("""
                                ]
                            }]
                        }
                    }
                );

                );

                </script>

                </body>
                </html>
                """);

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(html.toString());
        }
    }
}