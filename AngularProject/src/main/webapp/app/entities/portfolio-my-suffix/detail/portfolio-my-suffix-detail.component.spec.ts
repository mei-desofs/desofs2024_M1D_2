import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PortfolioMySuffixDetailComponent } from './portfolio-my-suffix-detail.component';

describe('PortfolioMySuffix Management Detail Component', () => {
  let comp: PortfolioMySuffixDetailComponent;
  let fixture: ComponentFixture<PortfolioMySuffixDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PortfolioMySuffixDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PortfolioMySuffixDetailComponent,
              resolve: { portfolio: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PortfolioMySuffixDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PortfolioMySuffixDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load portfolio on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PortfolioMySuffixDetailComponent);

      // THEN
      expect(instance.portfolio()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
