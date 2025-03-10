<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Clients</title>
</head>

<body>
<h4>Create new client</h4>
<form method="post" action="/api/clients" id="new_client">
    <label for="name">Name</label>
    <input type="text" id="name" name="name" placeholder="John Doe" required>

    <label for="address">Street</label>
    <input type="text" id="address" name="address" placeholder="Central street" required>

    <label for="phone">Phone</label>
    <input type="number" id="phone" name="phone" placeholder="79061234567" required>

    <input type="submit" value="Create new client">
</form>

<h4>List of clients</h4>
<table style="width: 400px">
    <thead>
        <tr>
            <td style="width: 50px">Id</td>
            <td style="width: 150px">Name</td>
            <td style="width: 100px">Street</td>
            <td style="width: 100px">Phone number</td>
        </tr>
    </thead>
    <tbody>
    <#list clients as client>
    <tr>
        <td>${client.id}</td>
        <td>${client.name}</td>
        <td>${client.address.street}</td>
        <td>${client.phones[0].number}</td>
    </tr>
    </#list>
    </tbody>
</table>
<script>
    const exampleForm = document.getElementById("new_client");
    exampleForm.addEventListener("submit", handleFormSubmit);

    async function handleFormSubmit(event) {
        event.preventDefault();

        const form = event.currentTarget;
        const url = form.action;

        try {
            const formData = new FormData(form);
            const responseData = await postFormDataAsJson({ url, formData });

            console.log({ responseData });

            window.location.href = "/clients";
        } catch (error) {
            console.error(error);
        }
    }

    async function postFormDataAsJson({ url, formData }) {
        const plainFormData = Object.fromEntries(formData.entries());
        const formDataJsonString = JSON.stringify(plainFormData);

        const fetchOptions = {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json",
            },
            body: formDataJsonString,
        };

        const response = await fetch(url, fetchOptions);

        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(errorMessage);
        }

        return response.json();
    }
</script>
</body>
</html>