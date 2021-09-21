<h1>Team Manager API</h1>
<h2>Project Description</h2>

The Team Manager API was created to provide coaches and players a way to interact asynchronously. Through the Team Manager app, players are able to join teams and view essential information directly from their coaches. Coaches are able to scout and recruit players to their team, while managing their current players. The API supplies users with endpoints that allow them to perform CRUD operations on data they are authorized for.
<h2>Technologies Used</h2>

    Java 8
    Spring - 2.5.4
    Spring Boot
    Spring Data
    Spring AOP
    Junit - 5
    Mockito - 2.5.4

<h2>Features</h2>

- Coaches can: create a team, add/remove players to a team, assign positions to players, and assign workouts to players.
- Recruiters can: Create an account and rate players skills.
- Players can: add skills/sports to profile, accept an offer to join a team, leave a team, and mark workouts that were assigned as completed.

<h2>To-do list:</h2>

- Store exercises in db to be passed to players for a better view of what is assigned
- Allow coaches to see players completed/uncompleted workouts
- Remove completed workouts after a set amount of time
- Give the app a makeover for styling

<h2>Getting Started</h2>

git clone https://github.com/ColbyDub/CLMW_api_p2.git

In order for the API to work as intended a user must have a database to interact with. The API is configured to work with mongodb. In order to include your mongodb in this project you need to include an application.yml file that includes the following:
- DB configurations: host address (x.xxx.xxx.xxx), port, name of database, username, password
* A variable named salt for password encryption
+ A variable named pinSalt for pin encryption
* JWT configuration: header, prefix, secret, expiration
<p>A note on pins: To be able to register, pins need to be included for both recruiter and coach. To do this utilize the PasswordUtils class and method "generateSecurePin" to generate a pin and insert it into a the collection named "pins" to be referenced later.</p>


<h2>Usage</h2>
<h3>/coach</h3>

- GET /{username} takes in a username in the path and returns a CoachDTO if found in db
- GET /player/{playerUsername} takes in playerUseranme and returns the team for said player
- POST /{pin} takes in a coach object in the body and a pin in the path. Save the coach object if pin and object are valid
- PUT /positions takes in an object coach and player usernames and a string position. Updates the position of the player
- PUT /team takes in an offer object and adds the player to the team
- PATCH /team/remove takes in an offer object and removes the player from the team
- PATCH /assign/{username} takes in a body with string exercise and assigns it to the team

<h3>/player</h3>

- GET / returns all players in the player service
- GET /{sport}   takes in a sport path variable and returns all players with that sport in their sport section
- GET /user/{username} takes in a username path variable and returns the playerDTO if found in db
- GET /exercises/{username} takes in a username path variable and returns the exercises of player if found
- POST / attempts to register the player given in request body
- PUT /{operation} takes in an offer and an "operation" in path to either extend or rescind an offer to a player
- PUT /skill takes in an object of playerUsername and skill in body and adds the skill to the player
- PUT /skill/rate assigns a value to the given skill of a player via request parameters
- PUT /skill/manage takes in username and skill to delete from a player
- PUT /sport takes in a sport to add to a player
- PUT /sport/manage takes in a sport to delete from a player
- PUT /exercise/{operation} takes in the exercise via body and either "complete" or "uncomplete" to move the exercise to completed or not

<h3>/recruiter</h3>

- POST /{pin} takes in a pin to verify the requesting user is authorized and attempts to register a recruiter with info in the body
<h3>/auth</h3>

- POST /coach & /recruiter & /player all take in a credentials object in body and attempt to login. Token is created from logged in user and returned to requester

<h2>Contributors</h2>

- Luna Haines
- Mitchell Panenko
- Bill Thomas
- Colby Wall
    
