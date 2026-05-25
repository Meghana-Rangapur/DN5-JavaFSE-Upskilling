/* ================================================================
   main.js — CivicPulse Community Event Portal
   JavaScript Module: All 14 exercises demonstrated here
   ================================================================ */


/* ================================================================
   JS Exercise 1: JavaScript Basics & Setup
   console.log, alert on page load
   ================================================================ */

// Log welcome message to console
console.log('%c🏙 Welcome to the Community Portal', 'color:#1a3a5c;font-size:1.1rem;font-weight:bold;');

// Alert on page fully loaded (DOMContentLoaded)
window.addEventListener('DOMContentLoaded', () => {
  alert('🏙 Welcome to CivicPulse — Community Event Portal!\n(JS Exercise 1: alert on page load)');
  loadSavedPreference();   // Ex 8: restore localStorage preference
  renderEvents(events);    // Ex 7: render events into DOM
  initJQuery();            // Ex 14: initialise jQuery handlers
  portalDebugInfo();       // Ex 13: log debug info to console
  console.log('localStorage contents:', { ...localStorage });
});


/* ================================================================
   JS Exercise 2: Syntax, Data Types, and Operators
   const, let, template literals, ++/--
   ================================================================ */

// const for fixed event info
const PORTAL_NAME  = 'CivicPulse';
const LAUNCH_DATE  = '2025-01-01';

// let for mutable state
let totalSeats = 100;
let bookedSeats = 42;
let availableSeats = totalSeats - bookedSeats;

// Template literals for event info display
const portalInfo = `Portal: ${PORTAL_NAME} | Launched: ${LAUNCH_DATE} | Seats available: ${availableSeats}`;
console.log('Ex 2 — Portal info:', portalInfo);

// ++/-- to manage seat count on registration
function incrementBooking() {
  bookedSeats++;
  availableSeats--;
  console.log(`Ex 2 — Seat booked. Booked: ${bookedSeats}, Available: ${availableSeats}`);
}

function decrementBooking() {
  if (bookedSeats > 0) {
    bookedSeats--;
    availableSeats++;
    console.log(`Ex 2 — Booking cancelled. Booked: ${bookedSeats}, Available: ${availableSeats}`);
  }
}


/* ================================================================
   JS Exercise 3: Conditionals, Loops, Error Handling
   if-else, forEach, try-catch
   ================================================================ */

// Events data array (used across exercises)
const events = [
  { id: 1, name: 'City Music Festival',  category: 'music',   date: '2025-08-15', seats: 200, fee: 'Free',  location: 'Central Park'    },
  { id: 2, name: 'Farmers Market',       category: 'food',    date: '2025-08-22', seats: 0,   fee: 'Free',  location: 'Town Square'     },
  { id: 3, name: 'Art Workshop',         category: 'art',     date: '2024-06-01', seats: 30,  fee: '₹200',  location: 'Community Hall'  },
  { id: 4, name: 'Charity 5K Run',       category: 'sports',  date: '2025-09-05', seats: 150, fee: '₹500',  location: 'Riverside Park'  },
  { id: 5, name: 'Food Festival',        category: 'food',    date: '2025-10-10', seats: 500, fee: '₹350',  location: 'Exhibition Grounds' },
  { id: 6, name: 'Tree Plantation Drive',category: 'nature',  date: '2025-09-21', seats: 80,  fee: 'Free',  location: 'Riverside Park'  },
];

// if-else: hide past or full events
function isEventValid(event) {
  const today = new Date();
  const eventDate = new Date(event.date);
  if (eventDate < today) {
    return { valid: false, reason: 'Past event' };
  } else if (event.seats === 0) {
    return { valid: false, reason: 'Fully booked' };
  } else {
    return { valid: true, reason: 'Available' };
  }
}

// forEach to loop and display events
function displayEventsToConsole(eventList) {
  console.log('Ex 3 — Looping through events:');
  eventList.forEach((event, index) => {
    const status = isEventValid(event);
    console.log(`  [${index + 1}] ${event.name} — ${status.reason}`);
  });
}
displayEventsToConsole(events);

// try-catch for registration error handling
function safeRegister(eventId, userName) {
  try {
    if (!userName || userName.trim() === '') throw new Error('User name cannot be empty.');
    const event = events.find(e => e.id === eventId);
    if (!event) throw new Error(`Event ID ${eventId} not found.`);
    const status = isEventValid(event);
    if (!status.valid) throw new Error(`Cannot register: ${status.reason}`);
    incrementBooking();
    console.log(`Ex 3 — ✅ ${userName} registered for "${event.name}"`);
    return true;
  } catch (err) {
    console.error('Ex 3 — Registration error:', err.message);
    return false;
  }
}


/* ================================================================
   JS Exercise 4: Functions, Scope, Closures, Higher-Order Functions
   addEvent, registerUser, filterEventsByCategory, closure counter
   ================================================================ */

// Add a new event (modifies the events array)
function addEvent(newEvent) {
  events.push(newEvent);
  console.log('Ex 4 — Event added:', newEvent.name);
  renderEvents(events);
}

// Register user for an event
function registerUser(eventId, userName, email) {
  return safeRegister(eventId, userName);
}

// Filter events by category (higher-order with callback)
function filterEventsByCategory(categoryList, filterFn) {
  return categoryList.filter(filterFn);
}

// Closure: track total registrations per category
function createCategoryCounter() {
  const counts = {};                      // private state via closure
  return {
    increment(category) {
      counts[category] = (counts[category] || 0) + 1;
      console.log(`Ex 4 — Category "${category}" registrations: ${counts[category]}`);
    },
    getCount(category) {
      return counts[category] || 0;
    },
    getAll() {
      return { ...counts };
    }
  };
}
const categoryCounter = createCategoryCounter();

// Pass callback to filter — dynamic search
function searchEventsByName(query) {
  return filterEventsByCategory(events, e =>
    e.name.toLowerCase().includes(query.toLowerCase())
  );
}


/* ================================================================
   JS Exercise 5: Objects and Prototypes
   Event constructor/class, prototype method, Object.entries
   ================================================================ */

// Event class with constructor
class EventModel {
  constructor(id, name, category, date, seats, fee, location) {
    this.id       = id;
    this.name     = name;
    this.category = category;
    this.date     = date;
    this.seats    = seats;
    this.fee      = fee;
    this.location = location;
  }
}

// checkAvailability added to prototype
EventModel.prototype.checkAvailability = function() {
  const today    = new Date();
  const eventDate = new Date(this.date);
  if (eventDate < today)    return '❌ Past event';
  if (this.seats === 0)     return '❌ Fully booked';
  return `✅ Available (${this.seats} seats left)`;
};

// Demo object — Object.entries
const sampleEvent = new EventModel(99, 'Demo Event', 'art', '2025-12-01', 50, '₹100', 'Demo Hall');
console.log('Ex 5 — Object.entries for sampleEvent:');
Object.entries(sampleEvent).forEach(([key, value]) => {
  console.log(`  ${key}: ${value}`);
});
console.log('Ex 5 — Availability:', sampleEvent.checkAvailability());


/* ================================================================
   JS Exercise 6: Arrays and Methods
   push, filter, map
   ================================================================ */

// .push() — add new event to array
function addNewEvent() {
  const newEv = { id: events.length + 1, name: 'Photography Walk', category: 'art',
                  date: '2025-11-10', seats: 25, fee: 'Free', location: 'Old Town' };
  events.push(newEv);
  console.log('Ex 6 — After push:', events.map(e => e.name));
}

// .filter() — show only music events
const musicEvents = events.filter(e => e.category === 'music');
console.log('Ex 6 — Music events:', musicEvents.map(e => e.name));

// .map() — format display cards
const formattedCards = events.map(e => `${getCategoryEmoji(e.category)} ${e.name} @ ${e.location}`);
console.log('Ex 6 — Formatted cards:', formattedCards);

function getCategoryEmoji(cat) {
  const map = { music:'🎵', food:'🍜', art:'🎨', sports:'🏃', nature:'🌳', market:'🥦' };
  return map[cat] || '📌';
}


/* ================================================================
   JS Exercise 7: DOM Manipulation
   querySelector, createElement, appendChild, UI updates
   ================================================================ */

// Render event cards into the DOM
function renderEvents(eventList) {
  const container = document.getElementById('eventList');
  if (!container) return;
  container.innerHTML = '';

  const validEvents = eventList.filter(e => isEventValid(e).valid);

  if (validEvents.length === 0) {
    container.innerHTML = '<p class="loading-msg">No events match your filter.</p>';
    return;
  }

  validEvents.forEach(event => {
    // createElement for each card
    const card = document.createElement('div');
    card.className = 'eventCard';
    card.setAttribute('data-id', event.id);
    card.setAttribute('data-category', event.category);

    card.innerHTML = `
      <h3>${getCategoryEmoji(event.category)} ${event.name}</h3>
      <p><strong>Date:</strong> ${formatDate(event.date)}</p>
      <p><strong>Location:</strong> ${event.location}</p>
      <p><strong>Seats:</strong> ${event.seats} &nbsp;|&nbsp; <strong>Fee:</strong> ${event.fee}</p>
      <button class="btn cta-button" style="margin-top:12px; padding:8px 18px; font-size:0.85rem;"
              onclick="handleRegisterFromCard(${event.id}, '${event.name}')">
        Register
      </button>
      <button class="btn btn-clear" style="margin-top:12px; padding:8px 14px; font-size:0.85rem; margin-left:8px;"
              onclick="handleCancelFromCard(${event.id}, this)">
        Cancel
      </button>
    `;

    container.appendChild(card);      // append to DOM
  });

  console.log('Ex 7 — DOM updated with', validEvents.length, 'event cards');
}

// Update UI when user registers from card
function handleRegisterFromCard(eventId, eventName) {
  const success = safeRegister(eventId, 'Portal User');
  if (success) {
    categoryCounter.increment(events.find(e => e.id === eventId)?.category || 'unknown');
    showToast(`✅ Registered for "${eventName}"!`);
  } else {
    showToast(`❌ Could not register for "${eventName}". Check console.`, true);
  }
}

// Update UI when user cancels
function handleCancelFromCard(eventId, btn) {
  decrementBooking();
  const card = btn.closest('.eventCard');
  if (card) card.style.opacity = '0.5';
  showToast('Registration cancelled.');
  console.log('Ex 7 — Cancelled registration for event ID:', eventId);
}

// Helper: format date string
function formatDate(dateStr) {
  const d = new Date(dateStr);
  return d.toLocaleDateString('en-IN', { day:'2-digit', month:'short', year:'numeric' });
}

// Helper: show temporary toast notification
function showToast(message, isError = false) {
  let toast = document.getElementById('toastMsg');
  if (!toast) {
    toast = document.createElement('div');
    toast.id = 'toastMsg';
    toast.style.cssText = `
      position:fixed; bottom:30px; right:30px; z-index:9999;
      padding:14px 24px; border-radius:10px; font-weight:600;
      font-family:'DM Sans',sans-serif; font-size:0.92rem;
      box-shadow:0 4px 20px rgba(0,0,0,.2); transition:opacity .4s;
    `;
    document.body.appendChild(toast);
  }
  toast.style.background = isError ? '#c0392b' : '#3b5e3a';
  toast.style.color = '#fff';
  toast.style.opacity = '1';
  toast.textContent = message;
  clearTimeout(toast._timer);
  toast._timer = setTimeout(() => { toast.style.opacity = '0'; }, 3000);
}


/* ================================================================
   JS Exercise 8: Event Handling
   onclick, onchange, keydown/keyup
   ================================================================ */

// onclick — "Register" button (called from HTML)
function handleRegisterClick(e) {
  e.preventDefault();
  console.log('Ex 8 — Register button clicked');
  submitRegistrationForm(e);
}

// onchange — filter events by category (called from HTML)
function filterByCategory(value) {
  console.log('Ex 8 — Category filter changed:', value);
  if (value === 'all') {
    renderEvents(events);
  } else {
    // Ex 6: using .filter()
    const filtered = events.filter(e => e.category === value);
    renderEvents(filtered);
  }
}

// onchange — event type selected in registration form
function handleEventTypeChange(value) {
  console.log('Ex 8 — Event type changed in form:', value);
}

// keyup — search events by name
function searchEvents(query) {
  console.log('Ex 8 — Keyup search:', query);
  if (query.length === 0) {
    renderEvents(events);
  } else {
    const results = searchEventsByName(query);
    renderEvents(results);
  }
}


/* ================================================================
   JS Exercise 9: Async JS — Promises, Async/Await
   Fetch from mock endpoint, .then()/.catch(), async/await, spinner
   ================================================================ */

// Mock API endpoint (JSONPlaceholder as stand-in)
const MOCK_API = 'https://jsonplaceholder.typicode.com/posts/1';

// Version 1: Using .then() and .catch()
function fetchWithPromise() {
  console.log('Ex 9 — Fetching with .then()/.catch()...');
  fetch(MOCK_API)
    .then(response => {
      if (!response.ok) throw new Error('Network response was not ok');
      return response.json();
    })
    .then(data => {
      console.log('Ex 9 — Data received (promise):', data.title);
    })
    .catch(err => {
      console.error('Ex 9 — Fetch error (promise):', err.message);
    });
}

// Version 2: Using async/await with loading spinner
async function fetchWithAsync() {
  console.log('Ex 9 — Fetching with async/await...');
  showSpinner(true);
  try {
    const response = await fetch(MOCK_API);
    if (!response.ok) throw new Error(`HTTP error: ${response.status}`);
    const data = await response.json();
    console.log('Ex 9 — Data received (async/await):', data.title);
  } catch (err) {
    console.error('Ex 9 — Fetch error (async/await):', err.message);
  } finally {
    showSpinner(false);
  }
}

function showSpinner(show) {
  let spinner = document.getElementById('loadingSpinner');
  if (!spinner) {
    spinner = document.createElement('div');
    spinner.id = 'loadingSpinner';
    spinner.style.cssText = `
      position:fixed; top:50%; left:50%; transform:translate(-50%,-50%);
      background:rgba(0,0,0,.7); color:#fff; padding:16px 28px;
      border-radius:10px; font-weight:600; z-index:9998;
      font-family:'DM Sans',sans-serif;
    `;
    spinner.textContent = '⏳ Loading...';
    document.body.appendChild(spinner);
  }
  spinner.style.display = show ? 'block' : 'none';
}

// Run both fetch versions on load
fetchWithPromise();
fetchWithAsync();


/* ================================================================
   JS Exercise 10: Modern JavaScript — ES6+ Features
   let/const, default parameters, destructuring, spread operator
   ================================================================ */

// Default parameters
function formatEventCard(name, category = 'general', fee = 'Free') {
  return `${getCategoryEmoji(category)} ${name} — Entry: ${fee}`;
}
console.log('Ex 10 — Default params:', formatEventCard('Community Meet'));
console.log('Ex 10 — With args:', formatEventCard('Art Expo', 'art', '₹200'));

// Destructuring — extract event details
const [firstEvent] = events;
const { name: firstName, date: firstDate, location: firstLocation } = firstEvent;
console.log(`Ex 10 — Destructured: "${firstName}" on ${firstDate} at ${firstLocation}`);

// Spread operator — clone event list before filtering (non-destructive)
function getSafeFilteredEvents(category) {
  const cloned = [...events];           // spread clone
  return cloned.filter(e => e.category === category);
}
console.log('Ex 10 — Spread + filter:', getSafeFilteredEvents('food').map(e => e.name));


/* ================================================================
   JS Exercise 11: Working with Forms
   form.elements, preventDefault, inline validation
   ================================================================ */

function submitRegistrationForm(e) {
  if (e && e.preventDefault) e.preventDefault();

  // Access form fields via form.elements
  const form    = document.getElementById('regForm');
  const name    = form.elements['fullName'].value.trim();
  const email   = form.elements['email'].value.trim();
  const date    = form.elements['eventDate'].value;
  const type    = form.elements['eventType'].value;
  const message = form.elements['message'].value.trim();

  // Clear previous errors
  clearFieldErrors();

  let hasError = false;

  // Inline validation
  if (!name) {
    showFieldError('nameError', 'Full name is required.');
    hasError = true;
  }
  if (!email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
    showFieldError('emailError', 'Please enter a valid email address.');
    hasError = true;
  }
  if (!date) {
    showFieldError('dateError', 'Please select a preferred date.');
    hasError = true;
  }
  if (!type) {
    showFieldError('typeError', 'Please select an event type.');
    hasError = true;
  }

  if (hasError) {
    console.log('Ex 11 — Form validation failed');
    return;
  }

  // Success — show in <output>
  const output = document.getElementById('formOutput');
  const selectedOption = form.elements['eventType'].options[form.elements['eventType'].selectedIndex].text;
  output.textContent = `✅ Thank you, ${name}! You're registered for "${selectedOption}" on ${formatDate(date)}. Confirmation sent to ${email}.`;

  console.log('Ex 11 — Form submitted:', { name, email, date, type, message });
  formDirty = false;   // reset dirty flag (HTML Ex 7)

  // Simulate seat update (Ex 2)
  incrementBooking();
  categoryCounter.increment(type);

  showToast(`✅ Registered successfully, ${name}!`);
}

function showFieldError(id, message) {
  const el = document.getElementById(id);
  if (el) el.textContent = message;
}

function clearFieldErrors() {
  ['nameError','emailError','dateError','typeError'].forEach(id => {
    const el = document.getElementById(id);
    if (el) el.textContent = '';
  });
}


/* ================================================================
   JS Exercise 12: AJAX & Fetch API
   fetch POST, success/failure message, setTimeout delay
   ================================================================ */

async function simulateFetchPost() {
  const resultDiv = document.getElementById('fetchResult');
  resultDiv.style.display = 'block';
  resultDiv.textContent = '⏳ Sending registration data...';

  // setTimeout to simulate delayed server response
  await new Promise(resolve => setTimeout(resolve, 1500));

  try {
    const payload = {
      name:  'Portal User',
      email: 'user@civicpulse.city',
      event: 'Community Event',
    };

    console.log('Ex 12 — POSTing payload:', payload);

    const response = await fetch('https://jsonplaceholder.typicode.com/posts', {
      method:  'POST',
      headers: { 'Content-Type': 'application/json' },
      body:    JSON.stringify(payload),
    });

    if (!response.ok) throw new Error(`Server error: ${response.status}`);

    const data = await response.json();
    console.log('Ex 12 — Server response:', data);

    resultDiv.style.borderLeftColor = '#3b5e3a';
    resultDiv.style.background = '#e6f4ea';
    resultDiv.style.color = '#3b5e3a';
    resultDiv.textContent = `✅ Registration sent successfully! Server assigned ID: ${data.id}`;
    showToast('✅ Fetch POST successful!');

  } catch (err) {
    console.error('Ex 12 — POST failed:', err.message);
    resultDiv.style.borderLeftColor = '#c0392b';
    resultDiv.style.background = '#fdecea';
    resultDiv.style.color = '#c0392b';
    resultDiv.textContent = `❌ Submission failed: ${err.message}`;
  }
}


/* ================================================================
   JS Exercise 13: Debugging and Testing
   Console logs, Network tab hints, breakpoints
   ================================================================ */

function portalDebugInfo() {
  // Place a DevTools breakpoint on the next line to inspect state
  const debugState = {                                 // ← breakpoint here
    portalName:      PORTAL_NAME,
    totalSeats:      totalSeats,
    bookedSeats:     bookedSeats,
    availableSeats:  availableSeats,
    eventsLoaded:    events.length,
    localStorage:    { ...localStorage },
    sessionStorage:  { ...sessionStorage },
  };

  console.groupCollapsed('Ex 13 — Portal Debug Info (expand to inspect)');
  console.table(debugState);
  console.log('Category counters:', categoryCounter.getAll());
  console.groupEnd();

  // Network tab: styles.css and main.js should appear under "All" tab
  console.log('Ex 13 — Check Network tab to verify styles.css and main.js loaded successfully');
}


/* ================================================================
   JS Exercise 14: jQuery
   $('#registerBtn').click, .fadeIn(), .fadeOut()
   ================================================================ */

function initJQuery() {
  if (typeof $ === 'undefined') {
    console.warn('Ex 14 — jQuery not loaded.');
    return;
  }

  console.log('Ex 14 — jQuery version:', $.fn.jquery);

  // jQuery click handler on register button
  $('#registerBtn').click(function(e) {
    console.log('Ex 14 — jQuery click on #registerBtn');
    // actual submission handled by submitRegistrationForm via handleRegisterClick
  });

  // jQuery click: toggle card visibility with fadeIn/fadeOut
  $('#jqFadeBtn').click(function() {
    const card = $('#jqCard');
    if (card.is(':visible')) {
      card.fadeOut(500);
      console.log('Ex 14 — jQuery .fadeOut() called');
    } else {
      card.fadeIn(500);
      console.log('Ex 14 — jQuery .fadeIn() called');
    }
  });

  // jQuery click: alert demo
  $('#jqAlertBtn').click(function() {
    alert('Ex 14 — jQuery click handler on #jqAlertBtn!\n\njQuery simplifies DOM traversal and event binding, but modern frameworks like React and Vue provide component-based architecture, reactive state management, and virtual DOM diffing for scalable apps.');
    console.log('Ex 14 — jQuery alert button clicked');
  });
}


/* ================================================================
   HTML Exercise 6 Handlers (called from HTML attributes)
   onblur, onchange, onclick, ondblclick, key events
   ================================================================ */

// onblur — phone validation
function validatePhone(input) {
  const cleaned = input.value.replace(/[\s\-\+()]/g, '');
  const msg = document.getElementById('phoneMsg');
  if (!input.value) {
    msg.textContent = '';
    input.classList.remove('error');
    return;
  }
  if (!/^\d{10,13}$/.test(cleaned)) {
    input.classList.add('error');
    msg.textContent = '⚠ Enter a valid 10–13 digit phone number.';
    msg.style.color = '#c0392b';
  } else {
    input.classList.remove('error');
    msg.textContent = '✔ Phone number looks good!';
    msg.style.color = '#3b5e3a';
  }
  console.log('Ex 6 — onblur phone validation:', input.value);
}

// onchange — show event fee
const EVENT_FEES = { music:'Free', market:'Free', art:'₹200', run:'₹500', food:'₹350', tree:'Free' };

function showEventFee(value) {
  const el = document.getElementById('feeDisplay');
  if (!el) return;
  if (!value) { el.style.display = 'none'; return; }
  el.style.display = 'inline-block';
  el.textContent = `Entry Fee: ${EVENT_FEES[value] || 'TBD'}`;
  console.log('Ex 6 — onchange event fee:', value, EVENT_FEES[value]);
}

// onclick — feedback submit
function submitFeedback() {
  const text  = document.getElementById('feedbackText').value.trim();
  const event = document.getElementById('feedbackEvent').value;
  const div   = document.getElementById('feedbackConfirm');
  if (!event) { showToast('⚠ Please select an event first.', true); return; }
  div.style.display = 'block';
  div.textContent = `✅ Thank you for your feedback on "${event}"!`;
  console.log('Ex 6 — onclick feedback submitted:', { event, textLength: text.length });
}

// ondblclick — zoom image
function toggleZoom(img) {
  img.classList.toggle('zoomed');
  console.log('Ex 6 — ondblclick image zoomed:', img.alt);
}

// key events — char counter
function countChars(textarea) {
  const len = textarea.value.length;
  const counter = document.getElementById('charCount');
  if (counter) counter.textContent = `${len} / 300 characters`;
}


/* ================================================================
   HTML Exercise 7 Handlers
   oncanplay, onbeforeunload
   ================================================================ */

// oncanplay — video ready
function videoReady() {
  const msg = document.getElementById('videoMsg');
  if (msg) msg.style.display = 'block';
  console.log('HTML Ex 7 — oncanplay: video ready to play');
}

// Track if registration form is dirty (onbeforeunload)
let formDirty = false;
document.addEventListener('DOMContentLoaded', () => {
  const regForm = document.getElementById('regForm');
  if (regForm) {
    regForm.querySelectorAll('input, select, textarea').forEach(el => {
      el.addEventListener('input', () => { formDirty = true; });
    });
  }
});

window.addEventListener('beforeunload', function(e) {
  if (formDirty) {
    e.preventDefault();
    e.returnValue = 'You have unsaved registration data. Are you sure you want to leave?';
    console.log('HTML Ex 7 — onbeforeunload triggered (form is dirty)');
  }
});


/* ================================================================
   HTML Exercise 8 Handlers
   localStorage, sessionStorage, clearPreferences
   ================================================================ */

function savePreference() {
  const val = document.getElementById('prefEvent').value;
  if (!val) { showToast('⚠ Please select an event type first.', true); return; }

  localStorage.setItem('preferredEvent', val);
  sessionStorage.setItem('sessionEvent', val);

  const savedMsg = document.getElementById('prefSaved');
  if (savedMsg) {
    savedMsg.style.display = 'block';
    setTimeout(() => { savedMsg.style.display = 'none'; }, 2500);
  }
  console.log('HTML Ex 8 — Saved to localStorage:', val);
}

function clearPreferences() {
  localStorage.clear();
  sessionStorage.clear();
  const prefSel = document.getElementById('prefEvent');
  if (prefSel) prefSel.value = '';
  const regSel = document.getElementById('eventType');
  if (regSel) regSel.value = '';
  showToast('🗑 All preferences cleared.');
  console.log('HTML Ex 8 — localStorage and sessionStorage cleared');
}

function loadSavedPreference() {
  const saved = localStorage.getItem('preferredEvent');
  if (saved) {
    const prefSel = document.getElementById('prefEvent');
    const regSel  = document.getElementById('eventType');
    if (prefSel) prefSel.value = saved;
    if (regSel)  regSel.value = saved;
    console.log('HTML Ex 8 — Loaded preference from localStorage:', saved);
  }
}


/* ================================================================
   HTML Exercise 9 Handler
   Geolocation: getCurrentPosition, error handling, high accuracy
   ================================================================ */

function findNearbyEvents() {
  const result = document.getElementById('geoResult');
  result.style.display = 'block';
  result.innerHTML = '⏳ Locating you...';

  if (!navigator.geolocation) {
    result.innerHTML = '❌ Geolocation is not supported by your browser.';
    return;
  }

  const options = {
    enableHighAccuracy: true,
    timeout:            8000,
    maximumAge:         0,
  };

  navigator.geolocation.getCurrentPosition(
    function onSuccess(pos) {
      const { latitude, longitude, accuracy } = pos.coords;
      result.innerHTML = `
        <strong>📍 Location Found!</strong><br>
        Latitude: <strong>${latitude.toFixed(5)}</strong><br>
        Longitude: <strong>${longitude.toFixed(5)}</strong><br>
        Accuracy: <strong>±${Math.round(accuracy)} metres</strong><br><br>
        🎯 <em>Nearest events to you:</em><br>
        🎵 City Music Festival — <strong>1.2 km</strong><br>
        🥦 Farmers Market — <strong>2.8 km</strong><br>
        🌳 Tree Plantation — <strong>3.4 km</strong>
      `;
      console.log('HTML Ex 9 — Geolocation success:', { latitude, longitude, accuracy });
    },
    function onError(err) {
      const messages = {
        1: '🚫 Permission denied. Please allow location access in browser settings.',
        2: '📡 Position unavailable. Could not determine your location.',
        3: '⏰ Request timed out. Please try again.',
      };
      result.innerHTML = messages[err.code] || '❌ Unknown geolocation error.';
      console.warn('HTML Ex 9 — Geolocation error:', err.message);
    },
    options
  );
}