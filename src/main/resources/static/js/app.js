console.log("App initialized");


document.getElementById("seeJobsButton").addEventListener("click", function () {
    fetch("/jobs") // Your get-mapping API endpoint
        .then(response => {
            if (!response.ok) {
                throw new Error("Network response was not OK");
            }
            return response.json(); // Parse the JSON response
        })
        .then(data => {
            // Clear the table before adding new data
            const tableBody = document.getElementById("jobsTableBody");
            tableBody.innerHTML = "";

            // Populate the table with data from the API response
            data.forEach(job => {
                const row = document.createElement("tr");

                const jobNameCell = document.createElement("td");
                jobNameCell.textContent = job.jobName;
                row.appendChild(jobNameCell);

                const jobDescriptionCell = document.createElement("td");
                jobDescriptionCell.textContent = job.jobDescription;
                row.appendChild(jobDescriptionCell);

                const jobLocationCell = document.createElement("td");
                jobLocationCell.textContent = job.jobLocation;
                row.appendChild(jobLocationCell);

                const experienceCell = document.createElement("td");
                experienceCell.textContent = job.yearsOfExperience;
                row.appendChild(experienceCell);

                const skillsCell = document.createElement("td");
                skillsCell.textContent = job.skills;
                row.appendChild(skillsCell);

                tableBody.appendChild(row);
            });
        })
        .catch(error => {
            console.error("There was a problem with the fetch operation:", error);
        });
});

