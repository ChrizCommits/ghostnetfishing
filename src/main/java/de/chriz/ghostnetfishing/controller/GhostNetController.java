package de.chriz.ghostnetfishing.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import de.chriz.ghostnetfishing.model.GhostNet;
import de.chriz.ghostnetfishing.model.GhostNetStatus;
import de.chriz.ghostnetfishing.model.User;
import de.chriz.ghostnetfishing.model.UserRole;
import de.chriz.ghostnetfishing.repository.GhostNetRepository;
import de.chriz.ghostnetfishing.error.DuplicateActiveNetException;
import de.chriz.ghostnetfishing.error.EmptyFieldException;

@Controller
public class GhostNetController {
	private final GhostNetRepository ghostNetRepository;

	// Konstruktor
	public GhostNetController(GhostNetRepository ghostNetRepository) {
		this.ghostNetRepository = ghostNetRepository;
	}

	// Bereitet alle Daten fürs Formulars vor
	@GetMapping("/report")
	public String showForm(Model model) {
		GhostNet ghostNet = new GhostNet();
		ghostNet.setUser(new User());
		model.addAttribute("ghostNet", ghostNet);
		model.addAttribute("formAction", "/report");
		model.addAttribute("pageTitle", "Geisternetz melden");
		return "report";
	}

	// Sendet die Daten des gemeldeten Netzes an die View
	@PostMapping("/report")
	public String submitReport(@ModelAttribute GhostNet ghostNet, Model model, RedirectAttributes ra) {
		if (ghostNet.getUser() == null) {
			ghostNet.setUser(new User());
		}

		// Setze Nutzerrollen und Netz-Status
		ghostNet.getUser().setRole(UserRole.MELDENDE_PERSON);
		ghostNet.setStatus(GhostNetStatus.GEMELDET);

		// Lies den Nutzer aus
		User user = ghostNet.getUser();

		Boolean anonym = user.getAnonym();

		if (anonym) {
			user.setName(null);
			user.setTelephone(null);
		} else {
			if (user.getName() != null && user.getName().isBlank())
				user.setName(null);
			if (user.getTelephone() != null && user.getTelephone().isBlank())
				user.setTelephone(null);
		}

		// Error-Handling
		Double latitude = ghostNet.getLatitude();
		Double longitude = ghostNet.getLongitude();
		List<GhostNetStatus> active = List.of(GhostNetStatus.GEMELDET, GhostNetStatus.BERGUNG_BEVORSTEHEND);
		boolean activeNetExists = ghostNetRepository.existsByLatitudeAndLongitudeAndStatusIn(latitude, longitude,
				active);

		Double size = ghostNet.getSize();
		String telephone = ghostNet.getUser().getTelephone();
		String name = ghostNet.getUser().getName();

		// Verhindere unausgefüllte Felder -> Error Page
		boolean isNetEmpty = latitude == null || longitude == null || size == null;
		boolean isUserEmpty = name == null || telephone == null;

		if (isNetEmpty || (isUserEmpty && !anonym)) {
			throw new EmptyFieldException();
		}

		// Schließe Duplikate aus -> Error Page
		boolean isDuplicate = latitude != null && longitude != null && activeNetExists;
		if (isDuplicate) {
			throw new DuplicateActiveNetException();
		}

		GhostNet saved = ghostNetRepository.save(ghostNet);

		// Gib die ID für das gemeldete Netz weiter
		ra.addFlashAttribute("reportedNetId", saved.getId());

		ra.addFlashAttribute("formAction", "/report");
		return "redirect:/report-success";
	}

	// Bereitet alle GhostNets aus der Datenbank für die View vor
	@GetMapping("/report-success")
	public String showSuccess(Model model, @RequestParam(defaultValue = "0") int reportSuccessPageIndex) {
		// Regelt die Zahl der Items der Pagination
		Pageable pageable = PageRequest.of(reportSuccessPageIndex, 10);
		Page<GhostNet> reportSuccessPage = ghostNetRepository.findAllByOrderByLastUpdatedDesc(pageable);

		model.addAttribute("reportSuccessPage", reportSuccessPage);
		model.addAttribute("reportSuccessNets", reportSuccessPage.getContent());

		// Pfade
		final String basePath = "/report-success";
		model.addAttribute("reportSuccessPath", basePath + "?reportSuccessPageIndex=");

		model.addAttribute("pageTitle", "Geisternetz erfolgreich gemeldet");
		return "report-success";
	}

	@GetMapping("/overview")
	public String showOverview(Model model, @RequestParam(defaultValue = "0") int reportedPageIndex,
			@RequestParam(defaultValue = "0") int recoveryPageIndex,
			@RequestParam(defaultValue = "0") int recoveredLostPageIndex) {

		// Regelt die Zahl der Items der Pagination
		final int pageSize = 5;
		Pageable pageableReported = PageRequest.of(reportedPageIndex, pageSize);
		Pageable pageableRecovery = PageRequest.of(recoveryPageIndex, pageSize);
		Pageable pageableRecoveredLost = PageRequest.of(recoveredLostPageIndex, pageSize);

		List<GhostNetStatus> ghostNetStatusList = List.of(GhostNetStatus.GEBORGEN, GhostNetStatus.VERSCHOLLEN);

		Page<GhostNet> reportedPage = ghostNetRepository.findByStatus(GhostNetStatus.GEMELDET, pageableReported);
		Page<GhostNet> recoveryPage = ghostNetRepository.findByStatus(GhostNetStatus.BERGUNG_BEVORSTEHEND,
				pageableRecovery);
		Page<GhostNet> recoveredLostPage = ghostNetRepository.findByStatusIn(ghostNetStatusList, pageableRecoveredLost);

		// Pagination-Objekte
		model.addAttribute("reportedPage", reportedPage);
		model.addAttribute("recoveryPage", recoveryPage);
		model.addAttribute("recoveredLostPage", recoveredLostPage);

		// Inhalt der Netz-Tabellen
		model.addAttribute("reportedNets", reportedPage.getContent());
		model.addAttribute("recoveryNets", recoveryPage.getContent());
		model.addAttribute("recoveredLostNets", recoveredLostPage.getContent());

		// Pfade
		final String basePath = "/overview";
		model.addAttribute("reportedPath", basePath + "?recoveryPageIndex=" + recoveryPageIndex
				+ "&recoveredLostPageIndex=" + recoveredLostPageIndex + "&reportedPageIndex=");
		model.addAttribute("recoveryPath", basePath + "?reportedPageIndex=" + reportedPageIndex
				+ "&recoveredLostPageIndex=" + recoveredLostPageIndex + "&recoveryPageIndex=");
		model.addAttribute("recoveredLostPath", basePath + "?reportedPageIndex=" + reportedPageIndex
				+ "&recoveryPageIndex=" + recoveryPageIndex + "&recoveredLostPageIndex=");

		// Anzeige der Netze für die Map
		List<GhostNet> reportedNetsList = ghostNetRepository.findByStatus(GhostNetStatus.GEMELDET);
		model.addAttribute("reportedNetsList", reportedNetsList);
		return "overview";
	}

	@GetMapping("/recover")
	public String showRecover(@RequestParam Long id, Model model) {
		GhostNet ghostNet = ghostNetRepository.findById(id).orElseThrow();
		// Erstelle einen neuen Nutzer, für leere Eingabefelder
		ghostNet.setUser(new User());

		model.addAttribute("ghostNet", ghostNet);
		model.addAttribute("formAction", "/recover");
		model.addAttribute("pageTitle", "Geisternetz bergen");
		return "recover";
	}

	@PostMapping("/recover")
	public String submitRecover(@ModelAttribute GhostNet ghostNet, @RequestParam Long id, RedirectAttributes ra) {
		GhostNet updatedNet = ghostNetRepository.findById(id).orElseThrow();

		// Errohandling - Falls kein User vorhanden ist
		if (updatedNet.getUser() == null) {
			throw new IllegalStateException();
		}

		updatedNet.setUser(ghostNet.getUser());

		updatedNet.getUser().setRole(UserRole.BERGENDE_PERSON);
		updatedNet.setStatus(GhostNetStatus.BERGUNG_BEVORSTEHEND);

		GhostNet saved = ghostNetRepository.save(updatedNet);
		Long recoveryNetId = saved.getId();

		// Gib die ID für das gemeldete Netz weiter
		ra.addFlashAttribute("recoveryNetId", recoveryNetId);

		return "redirect:/report-success";
	}

	@GetMapping("/report-recovered")
	public String showRecovered(@RequestParam Long id, Model model) {
		GhostNet ghostNet = ghostNetRepository.findById(id).orElseThrow();

		// Stabilität - Falls kein User vorhanden ist, wird ein neuer erstellt
		if (ghostNet.getUser() == null) {
			ghostNet.setUser(new User());
		}

		model.addAttribute("ghostNet", ghostNet);
		// Gib die Seitenkennung weiter
		model.addAttribute("formAction", "/report-recovered");
		model.addAttribute("pageTitle", "Geisternetz geborgen melden");
		return "report-recovered";
	}

	@PostMapping("/report-recovered")
	public String submitRecovered(@RequestParam Long id, @ModelAttribute GhostNet ghostNet, RedirectAttributes ra) {
		ghostNet = ghostNetRepository.findById(id).orElseThrow();
		// Sicherheitsprüfung des Nutzers
		User user = ghostNet.getUser();
		// wenn kein Nutzer vorhanden ist, erstelle einen neuen Nutzer
		if (user == null) {
			throw new IllegalStateException();
		}

		// Setze neuen Status fürs Netz
		ghostNet.setStatus(GhostNetStatus.GEBORGEN);
		// Speichere das Netz ins Repository ab
		GhostNet saved = ghostNetRepository.save(ghostNet);
		// Gib die ID für das gemeldete Netz weiter
		ra.addFlashAttribute("recoveredNetId", saved.getId());
		// Gib die Seitenkennung weiter
		ra.addFlashAttribute("formAction", "/report-recovered");
		return "redirect:/report-success";
	}

	public boolean isValidNetValue(Double netValue) {
		String stringValue = String.valueOf(netValue);
		String pattern = "^-?\\d+([.,]\\d+)?$";
		boolean isValidNetValue = stringValue.matches(pattern);
		return isValidNetValue;
	}
}
