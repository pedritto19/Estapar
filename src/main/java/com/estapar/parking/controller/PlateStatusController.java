@RestController
@RequestMapping("/plate-status")
public class PlateStatusController {

    @Autowired
    private PlateStatusService plateStatusService;

    @PostMapping
    public ResponseEntity<PlateStatusResponse> getPlateStatus(@RequestBody PlateStatusRequest request) {
        PlateStatusResponse response = plateStatusService.getStatus(request.getLicensePlate());
        return ResponseEntity.ok(response);
    }
}
