describe('Session spec', () => {
it('should login and see session list page', () => {
    cy.visit('/login');
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    });

    cy.intercept('GET', '/api/session', {
      body: [
        {
          id: 1,
          name: 'Session 1',
          description: 'Lorem ipsum dolor sit amet. ' +
              'Est incidunt omnis aut tenetur quasi ut ullam autem qui sunt iure. ' +
              'sed impedit quia id fuga galisum. Eum rerum doloribus quo ' +
              'dolorem culpa est rerum voluptas aut voluptas temporibus aut dolorem minima?',
          date: new Date(),
          createdAt: new Date(),
          updatedAt: new Date(),
          teacher_id: 1,
          users: [1, 2, 3],
        },
        {
          id: 2,
          name: 'Session 2',
          description: 'Lorem ipsum dolor sit amet. ' +
          'Est incidunt omnis aut tenetur quasi ut ullam autem qui sunt iure. ' +
          'sed impedit quia id fuga galisum. Eum rerum doloribus quo ' +
          'dolorem culpa est rerum voluptas aut voluptas temporibus aut dolorem minima?',
          date: new Date(),
          createdAt: new Date(),
          updatedAt: new Date(),
          teacher_id: 1,
          users: [1, 2, 4],
        },
      ],
    });
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(`${'test!1234'}{enter}{enter}`);
    cy.url().should('include', '/sessions')
  });

  it('should see the details', () => {
    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: 'Session 1',
        description: 'Lorem ipsum dolor sit amet. ' +
        'Est incidunt omnis aut tenetur quasi ut ullam autem qui sunt iure. ' +
        'sed impedit quia id fuga galisum. Eum rerum doloribus quo ' +
        'dolorem culpa est rerum voluptas aut voluptas temporibus aut dolorem minima?',
        date: new Date(),
        createdAt: new Date(),
        updatedAt: new Date(),
        teacher_id: 1,
        users: [1, 2, 3]
      },
    }).as('session');

    cy.intercept('GET', '/api/teacher/1', {
      body:{
        id: 1,
        lastName: 'Mario',
        firstName: 'Mario',
        createdAt: new Date(),
        updatedAt: new Date(),
        },
    }).as('teacher');

    cy.intercept('GET', '/api/session', {
      body: [
        {
          id: 1,
          name: 'Session 1',
          description: 'Lorem ipsum dolor sit amet. ' +
          'Est incidunt omnis aut tenetur quasi ut ullam autem qui sunt iure. ' +
          'sed impedit quia id fuga galisum. Eum rerum doloribus quo ' +
          'dolorem culpa est rerum voluptas aut voluptas temporibus aut dolorem minima?',
          date: new Date(),
          createdAt: new Date(),
          updatedAt: new Date(),
          teacher_id: 1,
          users: [1, 2, 3],
        },
        {
          id: 2,
          name: 'Session 2',
          description: 'Lorem ipsum dolor sit amet. ' +
          'Est incidunt omnis aut tenetur quasi ut ullam autem qui sunt iure. ' +
          'sed impedit quia id fuga galisum. Eum rerum doloribus quo ' +
          'dolorem culpa est rerum voluptas aut voluptas temporibus aut dolorem minima?',
          date: new Date(),
          createdAt: new Date(),
          updatedAt: new Date(),
          teacher_id: 1,
          users: [1, 2, 4],
        },
      ]
    }).as('sessions');

    cy.get('mat-card-actions button').first().click();
    cy.url().should('include', '/sessions/detail/1');
    cy.get('h1').contains('Session 1');
    cy.get('div.description').contains('Lorem ipsum dolor sit amet. ' +
    'Est incidunt omnis aut tenetur quasi ut ullam autem qui sunt iure. ' +
    'sed impedit quia id fuga galisum. Eum rerum doloribus quo ' +
    'dolorem culpa est rerum voluptas aut voluptas temporibus aut dolorem minima?');
    cy.get('mat-card-subtitle').contains('Mario MARIO')
  });

  it('should return to sessions list page', () => {
    cy.get('button').first().click();
    cy.url().should('include', '/sessions');
  });

  // Account page
  it('should go to the account page', () => {
    cy.intercept('GET', '/api/user/1', {
      id: 1,
      email: 'mario@test.com',
      lastName: 'Mario',
      firstName: 'Mario',
      admin: false,
      password: 'test!1234',
      createdAt: new Date(),
    });

    cy.get('.link').contains('Account').click();
    cy.url().should('include', '/me');

    cy.get('p').should('exist');
    cy.get('p').should('exist');
  });

  // Logout
  it('should logout', () => {
    cy.get('.link').contains('Logout').click();
    cy.url().should('contain', '/');
  });

  // Not Found
  it('should redirect to the not found page', () => {
    cy.visit('/notavalidurlforsure');
    cy.url().should('contain', '/404');
  });
});

describe('admin yoga-app', () => {

  it('should login', () => {
    cy.visit('/login');
    cy.server();
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept('GET', '/api/session', []).as('session')
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    cy.url().should('include', '/sessions');
  });

  it('create session', () => {
    cy.intercept('GET', '/api/teacher', {
      body: [
        {
          id: 1,
          lastName: 'Mario',
          firstName: 'Mario',
          createdAt: new Date(),
          updatedAt: new Date(),
        },
        {
          id: 2,
          lastName: 'Luigi',
          firstName: 'Mario',
          createdAt: new Date(),
          updatedAt: new Date(),
        }
      ]
    });
    cy.get('button').contains('Create').click();
    cy.url().should('include', '/sessions/create');

    cy.intercept('POST', '/api/session', {
      body: {
        id: 3,
        name: 'New session',
        description: 'New session description',
        teacher: 1,
        users: [],
      },
    }).as('session');

    cy.intercept('GET', '/api/session', {
      body: [
        {
          id: 1,
          name: 'Session 1',
          description:  'Lorem ipsum dolor sit amet. ' +
          'Est incidunt omnis aut tenetur quasi ut ullam autem qui sunt iure. ' +
          'sed impedit quia id fuga galisum. Eum rerum doloribus quo ' +
          'dolorem culpa est rerum voluptas aut voluptas temporibus aut dolorem minima?',
          date: new Date(),
          createdAt: new Date(),
          updatedAt: new Date(),
          teacher_id: 1,
          users: [1, 2, 3],
        },
        {
          id: 2,
          name: 'Session 2',
          description:  'Lorem ipsum dolor sit amet. ' +
          'Est incidunt omnis aut tenetur quasi ut ullam autem qui sunt iure. ' +
          'sed impedit quia id fuga galisum. Eum rerum doloribus quo ' +
          'dolorem culpa est rerum voluptas aut voluptas temporibus aut dolorem minima?',
          date: new Date(),
          createdAt: new Date(),
          updatedAt: new Date(),
          teacher_id: 1,
          users: [1, 2, 4],
        },
        {
          id: 3,
          name: 'New session',
          description: 'New session description',
          teacher: 1,
          users: [],
        },
      ],
    }).as('sessions');

    cy.get('input[formControlName=name]').type('New session');
    // set the date by clicking on the calendar
    cy.get('input[formControlName=date]').type('2024-01-10');
    // set the time by clicking on the clock
    cy.get('textarea[formControlName=description]').type(
        'New session description'
    );
    cy.get('mat-select[formControlName=teacher_id]').click();
    cy.get('mat-option').contains('Mario Mario').click();

    cy.get('button').contains('Save').click();
  });

  it('should see the new session', () => {
    cy.get('.item').should('have.length', 3);
    cy.get('.item').contains('New session').should('exist');
  });

  it('should see the details', () => {
    cy.intercept('GET', '/api/session/3', {
      body: {
        id: 3,
        name: 'New session',
        description: 'New session description',
        teacher: 1,
        users: [],
      },
    }).as('session');

    cy.intercept('GET', '/api/teacher/1', {
      body: {
        id: 1,
        lastName: 'Mario',
        firstName: 'Luigi',
        createdAt: new Date(),
        updatedAt: new Date(),
      },
    }).as('teacher');

    cy.get('mat-card').last().contains('Detail').last().click();
    cy.url().should('include', '/sessions/detail/3');

    cy.get('div').contains('New session description').should('exist');
  });

  it("should delete session", () => {
    cy.intercept('DELETE', '/api/session/3', {});
    cy.intercept('GET', '/api/session', {
      body: [{
        id: 1,
        name: 'Session 1',
        description: 'Lorem ipsum dolor sit amet. ' +
            'Est incidunt omnis aut tenetur quasi ut ullam autem qui sunt iure. ' +
            'sed impedit quia id fuga galisum. Eum rerum doloribus quo ' +
            'dolorem culpa est rerum voluptas aut voluptas temporibus aut dolorem minima?',
        date: new Date(),
        createdAt: new Date(),
        updatedAt: new Date(),
        teacher_id: 1,
        users: [1, 2, 3],
        },
        {
          id: 2,
          name: 'Session 2',
          description:  'Lorem ipsum dolor sit amet. ' +
          'Est incidunt omnis aut tenetur quasi ut ullam autem qui sunt iure. ' +
          'sed impedit quia id fuga galisum. Eum rerum doloribus quo ' +
          'dolorem culpa est rerum voluptas aut voluptas temporibus aut dolorem minima?',
          date: new Date(),
          createdAt: new Date(),
          updatedAt: new Date(),
          teacher_id: 1,
          users: [1, 2, 4],
        },],
    }).as('sessions');

    cy.get('button').contains('Delete').click();

    cy.url().should('contain', '/sessions');
    cy.get('.item').should('have.length', 2);
    cy.get('.item').contains('New session').should('not.exist');
  });

});