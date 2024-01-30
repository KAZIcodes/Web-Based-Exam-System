
    window.onload = () => {
    document.getElementById("Grade").href = window.location.href + "/grades";
    document.getElementById("glossary").href = window.location.href + "/glossary";
}

    let currentUrl = window.location.href;
    let urlParts = currentUrl.split("/");
    let classroomId = urlParts[urlParts.length - 1];


    document.getElementById("addTopic").onclick = () => {
    document.getElementById("newTopicName").style.display = "block";
    document.getElementById("saveNewTopic").style.display = "inline";
}

    document.getElementById("saveNewTopic").onclick = () => {

    sendData({newTitle: document.getElementById("newTopicName").value,
            addNew: true
        },
        {
            path:`/api/classrooms/${classroomId}/sections`,
            method:"PATCH",
            headers: [{
                key:"content-type",
                value:"application/json"
            }],
            json:true
        },
        (response) => {
            if (response.status === true) {
                setTimeout(function () {
                    window.location.reload();
                }, 1600);  // Redirect after 3 seconds
            }
            else
                showNotification(response.msg, "r");
        });
}

    function addTopics(topicName, activities, sectionId, count) {

    let card = document.createElement("div");
    card.className = "card";
    let cardHeader = document.createElement("div");
    cardHeader.className = "card-header";
    cardHeader.innerHTML = `
            <h5 class="mb-0">
                    <button class="btn btn-link btn-block text-left collapsed" type="button" data-bs-toggle="collapse"
                            data-bs-target="#resourceDropdown" aria-expanded="false">
                        ${topicName}
                    </button>
                    <span id="topicLink"><a id="editIconLink" href=""></span> <img src="/Pics/pencil-square.svg"
                                                                                    alt="" id=${"pencil" + count}></a>
                    <div id=${"editActivity" + count} class="edit-activity-form" >
                        <input type="text" id=${"newActivityName" + count} class="newActivityName" placeholder="Enter new topic name"/>
                        <button id=${"saveNewActivity" + count} class="btn btn-primary">Save</button>
                    </div>`;
    card.appendChild(cardHeader);

    let resourcesDrop = document.createElement("div");
    resourcesDrop.className = "collapse";
    resourcesDrop.id = "resourceDropdown";
    resourcesDrop.innerHTML = `
            <div class="card-body" id=${"parent" + count}>


                <div class="card-footer">

                    <button id="addActivity" type="button" class="btn btn-link" data-bs-toggle="modal" data-bs-target="#exampleModal">

                        <a id="plusIcon1" class="plus" href="#"> <img src="/Pics/plus-circle.svg" alt=""></a>
                        <i class="fas fa-plus"></i> Add another activity

                    </button>

                    <!-- Modal -->
                    <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h1 class="modal-title fs-5" id="exampleModalLabel">New activity</h1>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>

                                <div class="modal-body" style="background-color: #f2eff6;">

                                    <div class="addBox">
                                        <img id="addQuizIcon" src="/Pics/check-education-exam-svgrepo-com.svg" alt="" class="icon" />
                                        <button type="button" class="btn btn-activity" id="addQuiz">Add a New Quiz</button>
                                    </div>

                                    <div class="addBox">
                                        <img id="addPollIcon" src="/Pics/check-education-exam-svgrepo-com.svg" alt="" class="icon" />
                                        <button type="button" class="btn btn-activity" id="addPoll">Add a New Poll</button>
                                    </div>

                                    <div class="addBox">
                                        <img id="addAssignmentIcon" src="/Pics/check-education-exam-svgrepo-com.svg" alt="" class="icon" />
                                        <button type="button" class="btn btn-activity" id="addAssignment">Add a New Assignment</button>
                                    </div>

                                    <div class="addBox">
                                        <img id="addResourceIcon" src="/Pics/check-education-exam-svgrepo-com.svg" alt="" class="icon" />
                                        <button type="button" class="btn btn-activity" id="addResource">Add a New resource</button>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        `;
    card.appendChild(resourcesDrop);
    let hrElement = document.getElementById("superHR");
    hrElement.parentNode.insertBefore(card, hrElement.previousSibling);

    let firstChild = document.getElementById("parent" + count).firstChild;
    for (activity of activities){
    let aTag = document.createElement("a");
    aTag.className = "dropdown-item-box";
    aTag.href = window.location.href + "/sections/" + sectionId + "?AR=" + activity.id;
    aTag.innerText = activity.title;
    document.getElementById("parent" + count).insertBefore(aTag, firstChild);
}
}

    let sectionsList;
    sendData({},
    {
        path:`/api/classrooms/${classroomId}/sections`,
        method:"GET",
        headers: [{
        key:"content-type",
        value:"application/json"
    }],
        json:true
    },
    (response) => {
    if (response.status === true) {
    let count = 1;
    sectionsList = response.obj;
    for (section of sectionsList){
    addTopics(section.title, section.activities, section.id, count);

    document.getElementById("pencil" + count).onclick = (event) => {
    event.preventDefault();
    document.getElementById("newActivityName" + count).style.display= "inline";
    document.getElementById("saveNewActivity" + count).style.display= "inline";
};
    document.getElementById("saveNewActivity" + count).onclick = (event) => {
    event.preventDefault();
    let newTitle = document.getElementById("newActivityName" + count).value;
    sendData({newTitle: newTitle,
    sectionId: sectionsList[count - 1].id,
    addNew: false
},
{
    path:`/api/classrooms/${classroomId}/sections`,
    method:"PATCH",
    headers: [{
    key:"content-type",
    value:"application/json"
}],
    json:true
},
    (response) => {
    if (response.status === true) {
    setTimeout(function () {
    window.location.reload();
}, 1600);  // Redirect after 3 seconds
}
    else
    showNotification(response.msg, "r");
});
}
    count++;
}
}
});
