class SiteHeader extends HTMLElement {
  constructor() {
    super();
  }

  connectedCallback() {
    this.innerHTML =
      `<header>
        <div class="container">
          <div class="title">
            <a href="." id="home-button">DupDetector</a>
          </div>
        </div>
      </header>`;
                
  }
}

customElements.define('site-header', SiteHeader);