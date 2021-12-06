class SiteFooter extends HTMLElement {
  constructor() {
    super();
  }

  connectedCallback() {
    this.innerHTML =
      `<footer>
        <div class="container footer-container">
          <div class="sticky-bottom">
            <h3 style="text-align: center">Website created by Gabriel Lugo</h3>
            <img src="https://www.cs.odu.edu/~price/ODUseal.jpg" alt="ODU Seal" id="odu-seal">
          </div>
        </div>
      </footer>`;
  }
}

customElements.define('site-footer', SiteFooter);